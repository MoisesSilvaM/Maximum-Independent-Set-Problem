package model;

import java.io.IOException;
import java.util.ArrayList;

//Funcion ajena a ECJ, Se utiliza para probar que los terminales, métodos de otras funciones, y secuencias de alg. generados por PG, se ejecuten correctamente

public class Main {

	public static void main(String[] args) throws IOException {
		//ini prueba

		//prueba();
		//Alg_pg();
		
		//fin prueb
		
		FileIO.convert_cql_to_mis("data/all/clq");

	}
	//Secuencia de algoritmo generado
	public static void Alg_pg(){
		//Setup
		ArrayList<MISPData> data = new ArrayList<MISPData>();
		try {	FileIO.readInstances(data,"data/evolution");}	catch (IOException e) {	e.printStackTrace();}
		System.out.println("Actualizando: Estructuras variable y fijas...");
		for(int i=0; i<data.size();i++){data.get(i).getIndependenSet().UpdateIS_2();}
		
		//Inicio
		for(int i =0; i<data.size();i++){
			System.out.println("***Instancia["+(i+1)+"]: "+data.get(i).getInstance().getName()+"***");
			IndependentSet is = data.get(i).getIndependenSet(); 
			is.Print_Cants();
			
			//int num_veces = is.getnVert();
			Terminal.addFreeVertFewerNeighbors(is);
			
			while(Terminal.addFreeVertFewerFreeNeighbors(is)){
				
				if(Terminal.addFreeVertFewerNeighbors(is)){
					while(Terminal.addFreeVertFewerFreeNeighbors(is)){
						if(Terminal.addFreeVertFewerFreeNeighbors(is)){
							Terminal.removeSolVertMore1_TightNeighbors(is);
						}else{
							Terminal.OneImprovements2or3_Maximal(is);					
						}
					}
				}			
			}
		}
		//Fin
				
		System.out.println("FIN============================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]:\t\t");
			//data.get(i).getIndependenSet().Print_InfoMISP();
			data.get(i).getIndependenSet().Print_Cants();
		}
	}
	//metodo que prueba los terminales
	public static void prueba(){
		//prueba
		System.out.println("INICIO");
		long timeInit, timeEnd;				
		timeInit = System.nanoTime();	//inicio cronometro
		ArrayList<MISPData> data = new ArrayList<MISPData>();
		System.out.println("Leyendo todas las instancias...");
		try {	FileIO.readInstances(data,"data/evolution");}	catch (IOException e) {	e.printStackTrace();}
		System.out.println("Actualizando estructuras variable y fijas...");
		for(int i=0; i<data.size();i++){
			System.out.println("Actualizando: " + data.get(i).getInstance().getName());
			data.get(i).getIndependenSet().UpdateIS_2();//Actualiza y setea las estructuras fijas y variables
		}
		
		System.out.println("INICIO=========================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]: ");
			data.get(i).getIndependenSet().Print_Cants();
		}
		System.out.println("BLDR======================");
		for(int i =0; i<data.size();i++){
			System.out.println("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]: ");
			Terminal.Builder_for_FewerFreeNeighbors(data.get(i).getIndependenSet());
			data.get(i).getIndependenSet().Print_Cants();		
		}
		System.out.println("allImprv======================");
		for(int i =0; i<data.size();i++){
			System.out.println("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]: ");
			while(Terminal.OneImprovements2or3_Maximal(data.get(i).getIndependenSet()));
			data.get(i).getIndependenSet().Print_Cants();		
		}
		System.out.println("Estado============================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]:\t\t");
			//data.get(i).getIndependenSet().Print_InfoMISP();
			data.get(i).getIndependenSet().Print_Cants();
		}
		System.out.println("Meta======================");
		for(int i =0; i<data.size();i++){
			System.out.println("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]: ");
			Terminal.ILS(data.get(i).getIndependenSet());
			data.get(i).getIndependenSet().Print_Cants();		
		}
		
		timeEnd = System.nanoTime();	//fin cronometro
		System.out.println((timeEnd - timeInit) / 1000000 +" Milisegundos, "+((timeEnd - timeInit) / 1000000000)+" Seg");
		
		System.out.println("FIN============================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]:\t\t");
			//data.get(i).getIndependenSet().Print_InfoMISP();
			data.get(i).getIndependenSet().Print_Cants();
		}
		System.out.println("VALIDAR=======================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]:\t");
			boolean b = data.get(i).getIndependenSet().Validar_IS();
			System.out.println(b);
		}
		System.out.println("VALIDAR2=======================");
		for(int i =0; i<data.size();i++){
			System.out.print("("+(i+1)+"):["+data.get(i).getInstance().getName()+"]:\t");
			int b = data.get(i).getIndependenSet().Validar2_IS();
			System.out.println(b);
		}
		System.out.println("==============================");
		//fin prueba
	}
	
}