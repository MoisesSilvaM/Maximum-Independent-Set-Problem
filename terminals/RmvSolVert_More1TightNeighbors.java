package terminals;

import model.Terminal;
import model.MISPData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class RmvSolVert_More1TightNeighbors  extends GPNode {

private static final long serialVersionUID = -2534468225794857335L;
	
	public String toString() { return "Rmv_More1TightNeighb"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 0) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	@Override
	public void eval(final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		MISPData mispd = (MISPData) input;
		
		mispd.setResult(Terminal.removeSolVertMore1_TightNeighbors(mispd.getInstance().getIndependentSet()));
		//System.out.println("Rmv_More1TightNeighb - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
	}
}