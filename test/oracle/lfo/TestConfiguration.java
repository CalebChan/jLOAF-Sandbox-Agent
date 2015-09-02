package oracle.lfo;

import oracle.lfo.expert.LfOFixedSequenceTest;
import oracle.lfo.expert.LfOSmartExplorerTest;
import oracle.lfo.expert.LfOSmartRandomTest;
import oracle.lfo.expert.LfOSmartStraightLineTest;
import oracle.lfo.expert.LfOZigZagTest;

import org.jLOAF.retrieve.sequence.weight.DecayWeightFunction;
import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.GaussianWeightFunction;
import org.jLOAF.retrieve.sequence.weight.LinearWeightFunction;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;

public class TestConfiguration {
	public static final boolean TEST_ALL = false;
	public static final boolean USE_NON_RANDOM = false;
	
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
		
		new GaussianWeightFunction(0, 1.5),
		new GaussianWeightFunction(1, 1.5),
		new GaussianWeightFunction(2, 1.5),
		new GaussianWeightFunction(5, 1.5),
		new GaussianWeightFunction(10, 1.5),
		
	};
	
	public static final int MAX_RUNS = 1;
	
	public static final int PARAMETERS = 4;
	
	public static final int MAX_REPEATED_RUNS = 6;
	
	public static final String REASONINGS[] = {
		"SEQ",
		"KNN",
		"BEST"
	};
	
	public static final String MAP_NAMES[] = {
		,
	};
	
	public static final boolean USE_MAPS = true;
	public static final boolean USE_PREGEN_TRACE = true;
	
	public static final LfOAbstractCreatureTest test[] = {	
			new LfOSmartRandomTest(), 
			new LfOSmartStraightLineTest(), 
			new LfOZigZagTest(), 
			new LfOFixedSequenceTest(), 
			new LfOSmartExplorerTest()
			};
	
	private static final String BASE_MAP_DIR = "C:/Users/calebchan/Desktop/Stuff/workspace/LFOsimulator/maps/";
	
	public static final String MAP_LOCATION[] = {
		BASE_MAP_DIR + "discreet-8x8.xml",
		BASE_MAP_DIR + "discreet-8x8-2.xml",
		BASE_MAP_DIR + "discreet-8x8-3.xml",
		BASE_MAP_DIR + "discreet-8x8-4.xml",
		BASE_MAP_DIR + "discreet-8x8-5.xml",
		BASE_MAP_DIR + "discreet-32x32.xml",
		BASE_MAP_DIR + "discreet-32x32-2.xml"};
	
}
