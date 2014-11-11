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

import util.ParameterNameEnum;
import util.ParameterList;

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
	
	//private static final int[] K_VALUES = {4, 10, 20, 100};
	private static final int[] K_VALUES = {4};
	private static final int MAX_RUNS = 6;
	
	private static final int PARAMETERS = 4;
	
	private static final int MAX_REPEATED_RUNS = 6;
	
	private int repeatedNum;
	
	private static ParameterList list;
	
	public LfOTestSuite(int runNum, int kValue, boolean isRandom, int repeatedNum){
		this.repeatedNum = repeatedNum;
//		Config.RUN_NUMBER = runNum;
//		Config.K_VALUE = kValue;
//		Config.USE_RANDOM_KNN = isRandom;
//		Config.DEFAULT_TRACE_FOLDER = Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + Config.RUN_NUMBER;
//		Config.DEFAULT_EXPORT_RUN_FOLDER = Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((Config.USE_RANDOM_KNN) ? "Random" : "NonRandom") + "/Run " + Config.RUN_NUMBER + "/" + this.repeatedNum;
		
		list = new ParameterList();
		list.addParameter(ParameterNameEnum.ITER_NUM.name(), repeatedNum);
		list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), runNum);
		list.addParameter(ParameterNameEnum.K_VALUE.name(), kValue);
		list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), isRandom);
		list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + runNum);
		list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((isRandom) ? "Random" : "NonRandom") + "/Run " + runNum + "/" + this.repeatedNum);
		
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> testRuns(){
		
		Object o[][] = new Object[K_VALUES.length * MAX_RUNS * (MAX_REPEATED_RUNS + 1)][PARAMETERS];
		int index = 0;
		for (int i = 0; i < MAX_RUNS; i++){
			for (int j = 0; j < K_VALUES.length; j++){
				for (int k = 0; k < MAX_REPEATED_RUNS; k++){
					o[index] = new Object[]{i + 1, K_VALUES[j], true, k + 1};
					index++;
				}
				o[index] = new Object[]{i + 1, K_VALUES[j], false, 1};
				index++;
			}
		}
		
		return Arrays.asList(o);
	}
	
	@Test
	public void testAll(){
		System.out.println("Test Config : ");
		System.out.println("\tRun Number : " + list.getIntParam(ParameterNameEnum.RUN_NUMBER.name()));
		System.out.println("\tK Value : " + list.getIntParam(ParameterNameEnum.K_VALUE.name()));
		System.out.println("\tIs KNN Random : " + list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name()));
		System.out.println("\tRepeated Num : " + this.repeatedNum);
		System.out.println("Start Time : " + (new Timestamp(System.currentTimeMillis()).toString()));
		
		LfOAbstractTest test[] = {	
				new LfOSmartRandomTest(), 
				new LfOSmartStraightLineTest(), 
				new LfOZigZagTest(), 
				new LfOFixedSequenceTest(), 
				new LfOSmartExplorerTest()};
		
		for (LfOAbstractTest t : test){
			LfOAbstractTest.setParamters(LfOTestSuite.list);
			JUnitCore.runClasses(t.getClass());
		}
		
//		JUnitCore.runClasses(LfOSmartRandomTest.class, LfOSmartStraightLineTest.class, LfOZigZagTest.class, LfOFixedSequenceTest.class, LfOSmartExplorerTest.class);
		System.out.println("End Time : " + (new Timestamp(System.currentTimeMillis()).toString()) + "\n");
	}
}
