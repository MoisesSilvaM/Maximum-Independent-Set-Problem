package model;

//import java.util.ArrayList;

public class Instance {
	
	private String name;			//Nombre de la instancia
	private int optimal;			//El valor óptimo conocido para la instancias. Evaluado en 0 si no se conoce
	private int feasible;			//El mejor valor conocido para la instancia. Evaluado en 0 si ya se conoce el ópimo
	private boolean optimalKnown;	//Verdadero si el óptimo de la instancia es conocido.
	
	private IndependentSet IndependentSet;

	//CONSTRUCTOR
	public Instance() {
		this.optimal = 0;
		this.feasible = 0;
		this.optimalKnown = true;
		//this.name = "";
		//this.nVert = 0;
		//this.mArist = 0;
		//this.ListAdya = new ArrayList<ArrayList<Integer>>();
		IndependentSet = new IndependentSet();
	}

	//GETTERS
	public String getName() {		return name;	}
	public int getOptimal() {		return optimal;	}
	public int getFeasible() {		return feasible;	}
	public boolean isOptimalKnown() {		return optimalKnown;	}
	public IndependentSet getIndependentSet() {		return IndependentSet;	}

	//SETTERS
	public void setName(String name) {		this.name = name;	}
	public void setOptimal(int optimal) {	this.optimal = optimal;	}
	public void setFeasible(int feasible) {	this.feasible = feasible;	}
	public void setOptimalKnown(boolean optimalKnown) {	this.optimalKnown = optimalKnown;	}
	public void setIndependentSet(IndependentSet independentset) {	IndependentSet = independentset;	}
	public Instance clone() {	
		Instance i = new Instance();
		i.optimal = this.optimal;
		i.feasible = this.feasible;
		i.optimalKnown = this.optimalKnown;
		i.name = this.name;
		i.IndependentSet = this.IndependentSet.clone();
		return i;				
	}

	//TO STRING
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Instancia: " + name).append("\n");
		buffer.append("Cantidad Vertices: " + IndependentSet.getnVert()).append("\n");
		buffer.append("Cantidad Aristas: " + IndependentSet.getmArist()).append("\n");
		
		if(isOptimalKnown())// pregunta si el optimo es conocido
			buffer.append("Óptimo: " + optimal).append("\n");//true
		else
			buffer.append("Mejor: " + feasible);//false

		return buffer.toString();
	}

}
