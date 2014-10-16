package oracle.lfo;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import oracle.Config;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Parameterized.class)
//@SuiteClasses({ 
//	LfOSmartRandomTest.class,
//	LfOSmartStraightLineTest.class,
//	LfOZigZagTest.class, 
//	//LfOEqualFixedSequenceTest.class, 
//	LfOFixedSequenceTest.class,
//	LfOSmartExplorerTest.class })
@SuiteClasses(LfOTestSuite.class)
public class LfOTestSuite {
	
	private static final int[] K_VALUES = {4, 10, 20, 100};
	private static final int MAX_RUNS = 6;
	
	private static final int PARAMETERS = 3;
	
	public LfOTestSuite(int runNum, int kValue, boolean isRandom){
		Config.RUN_NUMBER = runNum;
		Config.K_VALUE = kValue;
		Config.USE_RANDOM_KNN = isRandom;
		Config.DEFAULT_TRACE_FOLDER = Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + Config.RUN_NUMBER;
		Config.DEFAULT_EXPORT_RUN_FOLDER = Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((Config.USE_RANDOM_KNN) ? "Random" : "NonRandom") + "/Run " + Config.RUN_NUMBER;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> testRuns(){
		
		Object o[][] = new Object[2 * K_VALUES.length * MAX_RUNS][PARAMETERS];
		int index = 0;
		for (int i = 0; i < MAX_RUNS; i++){
			for (int j = 0; j < K_VALUES.length; j++){
				o[index] = new Object[]{i + 1, K_VALUES[j], true};
				index++;
				o[index] = new Object[]{i + 1, K_VALUES[j], false};
				index++;
			}
		}
		
		return Arrays.asList(o);
	}
	
	@Test
	public void testAll(){
		System.out.println("Test Config : ");
		System.out.println("\tRun Number : " + Config.RUN_NUMBER);
		System.out.println("\tK Value : " + Config.K_VALUE);
		System.out.println("\tIs KNN Random : " + Config.USE_RANDOM_KNN);
		System.out.println("Start Time : " + (new Timestamp(System.currentTimeMillis()).toString()));
		JUnitCore.runClasses(LfOSmartRandomTest.class, LfOSmartStraightLineTest.class, LfOZigZagTest.class, LfOFixedSequenceTest.class, LfOSmartExplorerTest.class);
		System.out.println("End Time : " + (new Timestamp(System.currentTimeMillis()).toString()) + "\n");
	}
}
