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

public class IfThenElse extends GPNode {

	private static final long serialVersionUID = 1589755750993162501L;
	
	public String toString() { return "If_Then_Else"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 3) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
    	//Validations
		MISPData mispd = (MISPData) input;
		//MISProblemEvo mispp = (MISProblemEvo) problem;
		
		//Evalua al hijo izquierdo
		children[0].eval(state, thread, mispd, stack, individual, problem);
		//System.out.println("If_Izquierdo - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
		//Si es verdadero evaluar al hijo derecho 1
		if(mispd.getResult()) {
			children[1].eval(state, thread, mispd, stack, individual, problem);
			//System.out.println("If_Centro - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
			mispd.getResult();
			return;
		}
		//Si es falso evaluar al hijo derecho 2
		else {
			children[2].eval(state, thread, mispd, stack, individual, problem);
			//System.out.println("If_Derecho - "+mispd.getIndependenSet().getConjuntoSolucion()+" - "+mispd.getIndependenSet().getCurrentProfit()+ " - "+ mispd.getResult());
			mispd.getResult();
			return;
		}
    }
}
