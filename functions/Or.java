package functions;

import model.MISPData;
//import model.MISProblemEvo;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class Or extends GPNode {

	private static final long serialVersionUID = 1589755750993162501L;

	public String toString() { return "Or"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {

		super.checkConstraints(state, tree, typicalIndividual, individualBase);

		if (children.length != 2) {
			state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
		}
	}

	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {

		boolean x;
		MISPData mispd = (MISPData) input;
		//MISProblemEvo mispp = (MISProblemEvo) problem;

		children[0].eval(state, thread, input, stack, individual, problem);
		//System.out.println("Or_Izquierdo - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
		x = mispd.getResult();
		if(x) {
			mispd.setResult(true);
			return;
		}
		children[1].eval(state, thread, input, stack, individual, problem);
		//System.out.println("Or_Derecho - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
		mispd.setResult(mispd.getResult() || x);
	}
}