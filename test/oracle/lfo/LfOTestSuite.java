package oracle.lfo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import oracle.Config;
import oracle.lfo.test.log.TestSuiteDebugObserver;
import oracle.lfo.test.log.TestSuiteGeneralInfoLog;
import oracle.lfo.test.log.TestSuiteJSONLog;

import org.jLOAF.retrieve.SequenceRetrieval;
import org.jLOAF.retrieve.sequence.weight.FixedWeightFunction;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;
import org.jLOAF.util.JLOAFLogger;
import org.jLOAF.util.JLOAFLogger.Level;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Suite.SuiteClasses;

import util.ParameterNameEnum;
import util.ParameterList;

@RunWith(Parameterized.class)
@SuiteClasses(LfOTestSuite.class)
public class LfOTestSuite {
	
	private static final boolean TEST_ALL = false;
	private static final boolean USE_NON_RANDOM = false;
	
//	private static final int[] K_VALUES = {4, 10, 20, 100};
	private static final int[] K_VALUES = {4};
	
	private static final WeightFunction[] WEIGHT_FUNCTION = {
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
	
	private static final int MAX_RUNS = 1;
	
	private static final int PARAMETERS = 4;
	
	private static final int MAX_REPEATED_RUNS = 1;
	
	private int repeatedNum;
	
	private static ParameterList list;
	
	private static JLOAFLogger logger;
	private static int testNo = 1;
	private static TestSuiteDebugObserver ob;
	static{
		logger = JLOAFLogger.getInstance();
		try {
			ob = new TestSuiteDebugObserver("testSuiteLog.log");
			logger.addObserver(ob);
			logger.addObserver(new TestSuiteGeneralInfoLog());
			logger.addObserver(new TestSuiteJSONLog("testSuiteJSON.json"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LfOTestSuite(int runNum, int kValue, boolean isRandom, int repeatedNum, SequenceRetrieval r){
		this.repeatedNum = repeatedNum;
		
		list = new ParameterList();
		list.addParameter(ParameterNameEnum.ITER_NUM.name(), repeatedNum);
		list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), runNum);
		list.addParameter(ParameterNameEnum.K_VALUE.name(), kValue);
		list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), isRandom);
		list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + runNum);
		list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ((isRandom) ? "Random" : "NonRandom") + "/Run " + runNum + "/" + this.repeatedNum);
		list.addParameter(ParameterNameEnum.REASONING.name(), r);
		
	}
	
	@AfterClass
	public static void end(){
		ob.close();
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> testRuns(){
		int testSize = K_VALUES.length * MAX_RUNS * (MAX_REPEATED_RUNS + ((USE_NON_RANDOM) ? 1 : 0)) * WEIGHT_FUNCTION.length * ((TEST_ALL) ? 2 : 1);
		Object o[][] = new Object[testSize][PARAMETERS];
		int index = 0;
		for (int i = 0; i < MAX_RUNS; i++){
			for (int j = 0; j < K_VALUES.length; j++){
				// Default Weight
				if (TEST_ALL){
					for (int k = 0; k < MAX_REPEATED_RUNS; k++){
						o[index] = new Object[]{i + 1, K_VALUES[j], true, k + 1, null};
						index++;
					}
					if (USE_NON_RANDOM){
						o[index] = new Object[]{i + 1, K_VALUES[j], false, 1, null};
						index++;
					}
				}

			}
		}
		
		return Arrays.asList(o);
	}
	
	@Test
	public void testAll(){
		System.out.println("Test Config : ");
		//System.out.println("\tWeight : " +((WeightSequenceRetrieval)list.getParam(ParameterNameEnum.REASONING.name())).getWeightFunction().toString());
		System.out.println("\tRun Number : " + list.getIntParam(ParameterNameEnum.RUN_NUMBER.name()));
		System.out.println("\tK Value : " + list.getIntParam(ParameterNameEnum.K_VALUE.name()));
		System.out.println("\tIs KNN Random : " + list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name()));
		System.out.println("\tRepeated Num : " + this.repeatedNum);
		System.out.println("Start Time : " + (new Timestamp(System.currentTimeMillis()).toString()));
		
		
		
		LfOAbstractTest test[] = {	
				new LfOSmartRandomTest(), 
//				new LfOSmartStraightLineTest(), 
//				new LfOZigZagTest(), 
//				new LfOFixedSequenceTest(), 
//				new LfOSmartExplorerTest()
				};
		int i = 0;
		for (LfOAbstractTest t : test){
			logger.logMessage(Level.DEBUG, getClass(), "COUNT", "" + testNo + ":" + list.getIntParam(ParameterNameEnum.RUN_NUMBER.name()) + ":" + i);
			
			LfOAbstractTest.setParamters(LfOTestSuite.list);
			JUnitCore core = new JUnitCore();
			core.addListener(new TextListener(System.out));
			core.run(t.getClass());
			
			logger.logMessage(Level.DEBUG, getClass(), "END", "" + testNo + ":" + list.getIntParam(ParameterNameEnum.RUN_NUMBER.name()) + ":" + i);
			i++;
		}
		testNo++;
//		JUnitCore.runClasses(LfOSmartRandomTest.class, LfOSmartStraightLineTest.class, LfOZigZagTest.class, LfOFixedSequenceTest.class, LfOSmartExplorerTest.class);
		System.out.println("End Time : " + (new Timestamp(System.currentTimeMillis()).toString()) + "\n");
	}
}
