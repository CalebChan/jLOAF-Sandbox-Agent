package oracle.lfo;

import org.jLOAF.retrieve.sequence.weight.DecayWeightFunction;
import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.GaussianWeightFunction;
import org.jLOAF.retrieve.sequence.weight.LinearWeightFunction;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;

public class TestConfiguration {
	public static final boolean TEST_ALL = false;
	public static final boolean USE_NON_RANDOM = true;
	
	public static final int[] K_VALUES = {4, 10, 20, 100};
//	public static final int[] K_VALUES = {4};
	
	public static final WeightFunction[] WEIGHT_FUNCTION = {
		new FixedWeightFunction(1),
		
		new LinearWeightFunction(0.5),
		new LinearWeightFunction(0.2),
		new LinearWeightFunction(0.1),
		new LinearWeightFunction(0.05),
		
		new DecayWeightFunction(-10),
		new DecayWeightFunction(-1),
		new DecayWeightFunction(-0.1),
		new DecayWeightFunction(-0.01),
		
		new GaussianWeightFunction(1, 0.15),
		new GaussianWeightFunction(0.9, 0.15),
		new GaussianWeightFunction(0.8, 0.15),
		new GaussianWeightFunction(0.75, 0.15),
		new GaussianWeightFunction(0.5, 0.15),
		
	};
	
	public static final int MAX_RUNS = 1;
	
	public static final int PARAMETERS = 4;
	
	public static final int MAX_REPEATED_RUNS = 1;
	
	public static final String REASONINGS[] = {
		"SEQ",
		"KNN",
		"BEST"
	};
	
	public static final LfOAbstractCreatureTest test[] = {	
			new LfOSmartRandomTest(), 
			new LfOSmartStraightLineTest(), 
			new LfOZigZagTest(), 
			new LfOFixedSequenceTest(), 
			new LfOSmartExplorerTest()
			};
}
