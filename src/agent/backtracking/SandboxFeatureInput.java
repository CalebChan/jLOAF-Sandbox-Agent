package agent.backtracking;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class SandboxFeatureInput extends AtomicInput{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static SimilarityMetricStrategy simMet;
	
	public SandboxFeatureInput(String name, Feature f) {
		super(name, f);
	}

	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of deferred to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(SandboxFeatureInput.isClassStrategySet()){
			return SandboxFeatureInput.similarity(this, i);
		}else{
			return super.similarity(i);
		}
	}

	private static double similarity(Input complexInput, Input i) {
		return SandboxFeatureInput.simMet.similarity(complexInput, i);
	}

	public static boolean isClassStrategySet(){
		if(SandboxFeatureInput.simMet == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setClassSimilarityMetric(SimilarityMetricStrategy s){
		SandboxFeatureInput.simMet = s;
	}
}
