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

public class Builder_FewerFreeNeighbors extends GPNode {

	private static final long serialVersionUID = -2534468795456857335L;
	
	public String toString() { return "Bldr_FewrFNeighbors"; }
	
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
		
		mispd.setResult(Terminal.Builder_for_FewerFreeNeighbors(mispd.getInstance().getIndependentSet()));
		//System.out.println("Bldr_FewrFNeighbors - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
	}
}
