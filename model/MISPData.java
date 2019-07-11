package model;

import ec.gp.GPData;

public class MISPData extends GPData {
	
	private static final long serialVersionUID = 1236137301060685291L;

	protected boolean result;
	protected Instance instance;

	
	
	
	public MISPData(){
		result = false;
		instance = new Instance();
	}		
	@Override
	public MISPData clone() { //se crea una nueva variable MISPData con el mismo contenido, un CLON 
		MISPData clon = new MISPData();
		clon.result = this.result; 			//mismo result
		clon.instance = instance.clone();	//misma instancia
        return clon;
    }
	
    //GETTERS
	public boolean			getResult()				{	return result;		}
	public Instance			getInstance() 			{	return instance;	}
	public IndependentSet	getIndependenSet() 		{	return instance.getIndependentSet();	}
	//SETTERS
    public void 			setResult(boolean cond)				{	this.result = cond;		}
    public void 			setIndependenSet(IndependentSet is)	{	this.instance.setIndependentSet(is);	}
    public void 			setInstance(final Instance inst)	{	this.instance = (inst);	}
    
    //public String toString() {return ("[result=" + result + "]\n[instance=" + instance.toString(false) +"]\n[knapsack=" + instance.getIndependenSet().toString(false));}

}
