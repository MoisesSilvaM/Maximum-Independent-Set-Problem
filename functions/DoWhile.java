package functions;

import model.MISPData;

import java.util.List;

//import model.MISProblemEvo;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class DoWhile extends GPNode {

	private static final long serialVersionUID = 5933556719619805043L;

	public String toString() { return "While"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {

		super.checkConstraints(state, tree, typicalIndividual, individualBase);

		if (children.length != 2) {
			state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
		}
	}

	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		boolean x, y;
		int i, n;
		int lastProfit;
		
		MISPData mispd = (MISPData) input;
		//MISProblemEvo mispp = (MISProblemEvo) problem;

		children[0].eval(state, thread, mispd, stack, individual, problem);
		n = mispd.getInstance().getIndependentSet().getnVert();//por el numero de vertices de la instancia
		x = mispd.getResult();//resultado de la condicion
		i = 0;//numero de veces que se ha entrado al ciclo
		lastProfit = (int)mispd.getIndependenSet().getCurrentProfit();//= -1;
		List<Integer> Liste = mispd.getIndependenSet().getConjuntoSolucion();
		y = true;
		//System.out.println("While_Izquierdo - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
		//Revisar
		while(x && y && i < n) {//MIENTRAS X, Y e I menor a N, sean verdadero
			children[1].eval(state, thread, mispd, stack, individual, problem);
			//System.out.println("While_Derecho - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
			if(mispd.getIndependenSet().getCurrentProfit() == lastProfit) {
				if(mispd.getIndependenSet().getConjuntoSolucion().equals(Liste)){
					y = false;
				}
			}	
			else {
				lastProfit = (int)mispd.getIndependenSet().getCurrentProfit();
				Liste = mispd.getIndependenSet().getConjuntoSolucion();
				children[0].eval(state, thread, mispd, stack, individual, problem);
				//System.out.println("While_Izquierdo - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
				x = mispd.getResult();
				i++;
			}
		}
		//Si el while iteró al menos una vez se considera resultado verdaderos, caso contrario es falso
		if(i > 0)
			mispd.setResult(true);
		else
			mispd.setResult(false);
	}
}
