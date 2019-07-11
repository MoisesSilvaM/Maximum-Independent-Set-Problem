package functions;

import model.MISPData;
//import model.MISProblemEvo;
import ec.util.Parameter;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Repeat extends GPNode {

	private static final long serialVersionUID = 5933556666619805043L;

	public String toString() { return "Repeat"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 1) {
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
		double lastProfit;
		MISPData mispd = (MISPData) input;
		//MISProblemEvo mispp = (MISProblemEvo) problem;
		
		children[0].eval(state, thread, mispd, stack, individual, problem);
		
		n = mispd.getInstance().getIndependentSet().getnVert();
		x = mispd.getResult();
		i = 0;
		lastProfit = -1;
		y = true;
		
		while(x && y && i < n) {
			children[0].eval(state, thread, mispd, stack, individual, problem);
			if(mispd.getIndependenSet().getCurrentProfit() == lastProfit) {
				y = false;
			}
			else {
				lastProfit = mispd.getIndependenSet().getCurrentProfit();
				x = mispd.getResult();
				if(i==(n/2))System.out.println("Ejecucion atrapada: [Repeat]: "+children[0].toString());
				i++;
			}
		}
		if(i > 0)
			mispd.setResult(true);
		else
			mispd.setResult(false);
	}
}