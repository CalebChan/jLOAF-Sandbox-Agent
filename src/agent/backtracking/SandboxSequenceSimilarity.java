package agent.backtracking;

import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityInputMetricStrategy;

public class SandboxSequenceSimilarity implements SimilarityInputMetricStrategy{

	@Override
	public double similarity(Input i1, Input i2) {
		if (!(i1 instanceof SandboxFeatureInput) || !(i2 instanceof SandboxFeatureInput)){
			throw new IllegalArgumentException("Equality.similarity(...): One of the arguments was not an SandboxFeatureInput.");
		}
		
		SandboxFeatureInput s1 = (SandboxFeatureInput)i1;
		SandboxFeatureInput s2 = (SandboxFeatureInput)i2;
		if (!s1.getName().equals(s2.getName())){
			return 0;
		}
		
		return 1.0;
	}

}
