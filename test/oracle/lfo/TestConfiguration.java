package oracle.lfo;

import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;

public class TestConfiguration {
	public static final boolean TEST_ALL = false;
	public static final boolean USE_NON_RANDOM = false;
	
//	private static final int[] K_VALUES = {4, 10, 20, 100};
	public static final int[] K_VALUES = {4};
	
	public static final WeightFunction[] WEIGHT_FUNCTION = {
		new FixedWeightFunction(1),
//		
//		new FixedWeightFunction(0.9),
//		new FixedWeightFunction(0.5),
//		new FixedWeightFunction(0.1),
//		
//		new LinearWeightFunction(0.1),
//		new LinearWeightFunction(0.05),
//		
//		new DecayWeightFunction(-0.1),
//		new DecayWeightFunction(-1),
//		new DecayWeightFunction(10),
	};
	
	public static final int MAX_RUNS = 1;
	
	public static final int PARAMETERS = 4;
	
	public static final int MAX_REPEATED_RUNS = 1;
	
	public static final LfOAbstractCreatureTest test[] = {	
			new LfOSmartRandomTest(), 
//			new LfOSmartStraightLineTest(), 
//			new LfOZigZagTest(), 
//			new LfOFixedSequenceTest(), 
//			new LfOSmartExplorerTest()
			};
}
