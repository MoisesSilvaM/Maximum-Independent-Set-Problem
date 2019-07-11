package model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import ec.*;
import ec.gp.*;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleProblemForm;
import ec.util.*;

public class MISProblemEvo extends GPProblem implements SimpleProblemForm {

	private static final long serialVersionUID = -8430160211244271537L;
	public float MaxFitness=999;
	public static int RESULTS_FILE;
	public static int DOT_FILE;
	public static int JOB_NUMBER;
	public static String RESULTS_namefile = "MISPResults.out";
	public static String DOT_namefile = "BestIndividual.dot";
	public static String IN_namefile = "BestIndividual.in";

	public static long startGenerationTime;
	public static long endGenerationTime;

	public static final double IND_MAX_REL_ERR = 0.001;
	public static final double IND_IDEAL_NODES = 21;
	public static final double POND_ERP = 1.0;
	public static final double POND_ERL = 0.0;//1-POND_ERP;
	
	public static String semillas;
	//public static int cantidad_intancias_coevo = 6;//Co-evoluci�n
	public static String Outputpath = "out/results/evolution";
	public static String Instacespath =  "data/evolution";
	public static String dotexepath = "graphviz-2.38/bin/dot.exe";
	public static String statfile = "Statistics.out";
	public static String statfileCSV = "BestFitness.csv";
	
	public static int elites;//cantadidad de individuos elites que pasan a la siguiente generacion
	//public static boolean elitismo = true;//variable que indica si hay elitismo
	public static ArrayList<Generacion> Corrida;//Estructura que almacena todos los individuos de todas las generaciones
	ArrayList<MISPData> data;//Estructura que almacena las instancias

	@Override
	public MISProblemEvo clone() {
		MISProblemEvo mispp = (MISProblemEvo) super.clone();
		return mispp;
	}

	@Override
	public void setup(final EvolutionState state, final Parameter base) {
		// very important, remember this
		JOB_NUMBER = ((Integer)(state.job[0])).intValue();
		super.setup(state, base);
		// verify our input is the right class (or subclasses from it)
		if (!(input instanceof MISPData))
			state.output.fatal("GPData class must subclass from " + MISPData.class, base.push(P_DATA), null);
				
		//Cantidad de individuos elites que pasan a la siguiente generaci�n
		elites=  state.parameters.getInt(new ec.util.Parameter("breed.elite.0"),null);	
		//Se guardan las  semillas...�como?
		semillas=  state.parameters.getString(new ec.util.Parameter("seed.0"),null);
		
		//Se Lee la instancia desde archivo
		System.out.println("Obteniendo instancias de prueba...");	
		try {
			File evofolder = new File(Outputpath+MISProblemEvo.JOB_NUMBER+"/");//carpeta de archivos de la evoluci�n
			evofolder.mkdir();
			RESULTS_FILE = FileIO.newLog(state.output, Outputpath+MISProblemEvo.JOB_NUMBER+"/"+RESULTS_namefile);
			DOT_FILE = FileIO.newLog(state.output, Outputpath+MISProblemEvo.JOB_NUMBER+"/"+DOT_namefile);
			data = new ArrayList<MISPData>();//DATA	
			FileIO.readInstances(data, Instacespath);
		} catch (Exception e) {	e.printStackTrace();}
		System.out.println("Lectura de los "+ data.size() +" archivo terminada con �xito!");
		//Se actualiza y setea el resto de las variables
		System.out.println("Actualizando estructuras variables y fijas......");
		Setear_Instancias();
		Inicializa_Corrida(state.numGenerations);//estructura que alamacena los individuos
		System.out.println("MISProblem: Evolucionando...");
		startGenerationTime = System.nanoTime();	//inicio cron�metro evoluci�n
	}
	//Eval�a a un individuo
	@Override
	public void evaluate(
			final EvolutionState state,
			final Individual individual,
			final int subpopulation,
			final int threadnum) {

		if (!individual.evaluated) {
			
			ArrayList<MISPData> auxData = new ArrayList<MISPData>();
			GPIndividual gpind = (GPIndividual) individual;

			int hits = 0;
			double relErrAcum = 0.0;
			long timeAcum = 0;
			double instanceRelativeError = 0.0;
			int alturaArbol = gpind.trees[0].child.depth();//altura del arbol
			long tama�oArbol = gpind.size();//tama�o del arbol, numero de nodos
			double currentProfit = 0.0;
			//int avance_de_instancias = (state.generation)/50;// Co-evoluci�n, a las 10 cambia 0, a las 50 cambia 1, a las 100 cambia 2,.... a las 300 cambia 6, a las 350 cambia 7, a las 400 cambia 8... 
			//System.out.println("Avance: "+avance_de_instancias);// Co-evoluci�n
			int cantidad_intancias = data.size();
			//cantidad_intancias= cantidad_intancias_coevo; // Co-evoluci�n, subconjunto de instancias
			//int avance_en_rangos_de_cant = avance_de_instancias%(cantidad_intancias+1);// Co-evolucion, nos dice que instancia debe entrar entre [1-6] ===> 3%7=3, 6%7=6, 7%7=0, 8%7=1,13%7=6, 14%7=0, 
			double ERL = Relative_Error_Legibility(tama�oArbol);//Error Relativo de Legibilidad 
			
			//INICIO Evaluaci�n del algoritmo
			for(int i = 0; i < cantidad_intancias; i++) {
				auxData.add(data.get(i).clone()); //el individuo se enfrenta a una instancia limpia 
				//auxData.add(data.get(i+avance_en_rangos_de_cant).clone());	//Co-evoluci�n, se clona la instancia original para evaluar al individuo con una instancia limpia, sin modificaciones
				gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;	//escribir individuos en formato dot

				long timeInit, timeEnd;				
				timeInit = System.nanoTime();	//inicio cronometro
				//Se evalua al individuo con la instancia i
				gpind.trees[0].child.eval(state, threadnum, auxData.get(i), stack, gpind, this);	
				timeEnd = System.nanoTime();	//fin cronometro
				
				double duracion = (timeEnd - timeInit) / 1000000;//en milisegundos
				currentProfit = auxData.get(i).getIndependenSet().getCurrentProfit();//soluci�n obtenida
				double bestProfit = Best_Profit(auxData.get(i).getInstance());//Optimo o Mejor soluci�n conocida
				instanceRelativeError = Relative_Error(auxData.get(i).getInstance());//Error Relativo
				double err = bestProfit - currentProfit;
System.out.println(duracion);
				if(duracion > auxData.get(i).getIndependenSet().getnVert()){
					relErrAcum = duracion+auxData.get(i).getIndependenSet().getnVert()*(auxData.size()-i);
					//System.out.println(relErrAcum);
					break;
				}
				//Hits
				
				if(err == 0) { hits++; //instanceRelativeError < IND_MAX_REL_ERR || 	
				//System.out.println(auxData.get(i).getInstance().getName());
				}
				//Time
				
				//if((duracion/1000)>3) {	instanceRelativeError=1;	}//penalizacion por tiempo excesivo, en casos de prueba

				//[Generation] [Individual ID] [Exec Time] [Profit] [Optimal] [Rel Error Optimus] [Depth] [Tree Size] [Max Nodes] [Rel Error Nodes] [Fitness] [Hits]
				state.output.print(state.generation + " " + gpind.toString() + " ", RESULTS_FILE);
				state.output.print(duracion + " ", RESULTS_FILE);
				state.output.print(currentProfit + " " + bestProfit + " " + instanceRelativeError + " ", RESULTS_FILE);
				state.output.print(alturaArbol + " " + tama�oArbol + " " + IND_IDEAL_NODES + " " + ERL + " ", RESULTS_FILE);
				state.output.println(( POND_ERP*instanceRelativeError + POND_ERL*ERL+ " " + hits), RESULTS_FILE);
				// aqui voy a guardar solo los hits
				
				relErrAcum += instanceRelativeError;//acumulador del error relativo
				timeAcum+=duracion;//acumulador del tiempo de evaluacion

			}
			//FIN Evaluaci�n del algoritmo con todas las intancias

			Runtime garbage = Runtime.getRuntime();
			garbage.gc();

			double ERP = relErrAcum / auxData.size();//Error Relativo Promedio
			KozaFitness f = ((KozaFitness) gpind.fitness);
			float Fitness = (float)((POND_ERP*ERP) + (POND_ERL*ERL));
			f.setStandardizedFitness(state,Fitness);
			f.hits = hits;
			//gpind.evaluated = true;//Co-evoluci�n, todo se deben volver a evaluar, para enfrentarlos a las nuevas instancias
			gpind.evaluated = true; // siempre y cuando las instancias sea fijas
			float AdjustedFitness = (float) (1.0/(1.0 + Fitness));
			long tiempoPromedio = (timeAcum/auxData.size());
			int gen = state.generation;
			String id = gpind.toString();
			//System.out.println(gen+", "+id+", "+AdjustedFitness+", "+Fitness+", "+ERP+", "+ERL+", "+hits+", "+tiempoPromedio+", "+tama�oArbol+", "+alturaArbol);
			if(Fitness<MaxFitness) {
				MaxFitness=Fitness;
				AgregarIndv(gen,id,AdjustedFitness,Fitness,ERP,ERL, hits,tiempoPromedio,tama�oArbol,alturaArbol);
			}
		}		
	}

	@Override
	public void describe(final EvolutionState state,
			final Individual individual,
			final int subpopulation,
			final int threadnum,
			final int log) {
		endGenerationTime = System.nanoTime();	//fin cronometro evoluci�n
		long duracion = (endGenerationTime - startGenerationTime) / 1000000;
		state.output.message("Evolution duration: " + duracion + " ms");	//duraci�n evoluci�n en ms
		state.output.print(duracion+ "", RESULTS_FILE);
		System.out.println("Duraci�n: "+(duracion/(1000*60))+" minutos");

		//Print BestIndividual.in
		PrintWriter dataOutput = null;
		Charset charset = Charset.forName("UTF-8");
		try {
			dataOutput = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Outputpath+MISProblemEvo.JOB_NUMBER+"/job." + (MISProblemEvo.JOB_NUMBER) + "."+IN_namefile), charset)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataOutput.println(Population.NUM_SUBPOPS_PREAMBLE + Code.encode(1));
		dataOutput.println(Population.SUBPOP_INDEX_PREAMBLE + Code.encode(0));
		dataOutput.println(Subpopulation.NUM_INDIVIDUALS_PREAMBLE + Code.encode(1));
		dataOutput.println(Subpopulation.INDIVIDUAL_INDEX_PREAMBLE + Code.encode(0));
		individual.evaluated = false;
		((GPIndividual)individual).printIndividual(state, dataOutput);
		dataOutput.close();

		//Print BestIndividual.dot
		GPIndividual gpind = (GPIndividual) individual;
		gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;
		String indid = gpind.toString().substring(19);
		state.output.println("label=\"Individual=" + indid + " Fitness=" + ((KozaFitness) gpind.fitness).standardizedFitness() + " Hits=" + ((KozaFitness) gpind.fitness).hits + " Size=" + gpind.size() + " Depth=" + gpind.trees[0].child.depth() + "\";", DOT_FILE);
		gpind.printIndividualForHumans(state, DOT_FILE);


		String path = Outputpath+MISProblemEvo.JOB_NUMBER+"/job."+MISProblemEvo.JOB_NUMBER+".";
		try {
			elite();
			FileIO.repairDot(path,DOT_namefile);
			FileIO.dot_a_png(path,dotexepath,DOT_namefile);
			FileIO.Stat_a_csv(path,statfile,statfileCSV);
			FileIO.Estadistica_Promedio_y_Mejores(path,Corrida,"EstadisticaProm&Mej.csv");
			FileIO.Estadistica_Todos(path,Corrida,"Estadistica_Todos.csv");//agregar tiempo
			FileIO.GuardarSemillas(path,semillas,state.evalthreads,state.breedthreads,"Semillas.csv");//incompleta
		} catch (Exception e) {e.printStackTrace();}


	}
	public static double Relative_Error_Legibility(long tam){//El error depende de que tan alejado esta del tama�o ideal, sea mayor o menor
		double error_leg = Math.abs(tam - IND_IDEAL_NODES) / IND_IDEAL_NODES;
		return error_leg;
	}

	public static double Relative_Error(Instance inst){//Retorna Error Relativo, de un individuo
		double error_rel = 0;
		boolean opt = inst.isOptimalKnown();
		double currentProfit = inst.getIndependentSet().getCurrentProfit();
		if(opt){
			double optimo = inst.getOptimal();
			if(optimo == 0.0) {
				//System.out.println(" Optimo = 0 " + inst.getName());
				error_rel = currentProfit;
				//System.out.println("optimo: "+optimo + " - currentProfit" + currentProfit);
			}
			else {
				error_rel = Math.abs(optimo - currentProfit)/(optimo);//error normalizado, porcentaje de error //abs
			}
		}else{
			double feasible = inst.getFeasible();
			if(feasible == 0.0) {
				error_rel = currentProfit;
				
				//System.out.println("feasible: "+feasible + " / currentProfit" + currentProfit);
			}
			else {
				error_rel = Math.abs(feasible - currentProfit)/(feasible);//error normalizado, porcentaje de error
			}
		}			
		return error_rel;
	}
	public static double Best_Profit(Instance inst){//Retorna el Optimo o Mejor soluci�n concida
		boolean opt = inst.isOptimalKnown();
		if(opt)
			return inst.getOptimal();
		else
			return inst.getFeasible();
	}
	

	private void Setear_Instancias(){
		for(int i=0; i<data.size();i++){
			data.get(i).getIndependenSet().UpdateIS_2();//Actualiza y setea las estructuras fijas y variables
		}
	}
	private void Inicializa_Corrida(int numgen){
		Corrida = new ArrayList<Generacion>();		
		for(int i=0;i<numgen;i++){
			Corrida.add(new Generacion());
			}
	}
	private void AgregarIndv(int gen, String id,float adjs,float stand, double erp, double erl, int hits,long time,long nodos,int altura){
		Corrida.get(gen).AgregarIndv(gen,id,adjs,stand,erp,erl, hits,time,nodos,altura);
	}
	private void elite(){//agrega el mejor de cada generaci�n a la siguiente generaci�n, solo uno
		if(elites>0){
		//if(elitismo){
			int c = Corrida.size()-1;
			Individuo best;
			for(int i=0;i<c;i++){
				//System.out.print(i + " - ");
				best = Corrida.get(i).TheBestEvo();
				Corrida.get(i+1).AgregarIndv(best);
			}
		}
	}
}

