package oracle.backtracking.knntb;

import java.io.File;
import java.util.List;

import oracle.Config;
import oracle.SandboxOracle;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.jLOAF.tools.LeaveOneOut;
import org.jLOAF.tools.TestingTrainingPair;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.ParameterList;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;

public class KNNTBTest{

	public static final String CASEBASE_NAME = "kNN_TB_TestRes/kNNTest";
	public static final String TRACE_NAME = "kNN_TB_TestRes/Case1";
	public static final int DEFAULT_LENGTH = 9;
	public static final int NUM_OF_SIM = 5;
	
	public static final int DEFAULT_K = 2;
	public static final boolean USE_RANDOM_KNN = false;
	
	protected static List<TestingTrainingPair> loo;
	protected SandboxOracle oracle;
	protected SandboxAgent agent;
	
	@BeforeClass
	public static void init() throws Exception{
		DefaultLfOExpertStrategy expert = new DefaultLfOExpertStrategy();
		expert.parseFile(TRACE_NAME, CASEBASE_NAME);

		LeaveOneOut l = LeaveOneOut.loadTrainAndTest(CASEBASE_NAME + Config.CASEBASE_EXT, DEFAULT_LENGTH, NUM_OF_SIM);
		loo = l.getTestingAndTrainingSets();
		
	}
	
	@AfterClass
	public static void cleanUp() throws Exception{
		if (!Config.DELETE_TRACE){
			return;
		}
		
		File f = new File(CASEBASE_NAME + Config.CASEBASE_EXT);
		f.delete();
	}
	
	@Before
	public void setUp() throws Exception{
		CaseBase cb = loo.get(NUM_OF_SIM - 1).getTraining();
		Assert.assertFalse(cb == null);
		
		agent = new SandboxAgent(cb, true, DEFAULT_K, USE_RANDOM_KNN);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		oracle = new SandboxOracle(Config.DEFAULT_WORLD_SIZE, null, agent, creature, new LfOPerception(), new ParameterList());
		oracle.setTestData(loo.get(NUM_OF_SIM - 1).getTesting());
	}

	@Test
	public void testCase(){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		boolean same = oracle.testAgent(loo.get(NUM_OF_SIM - 1).getTesting().getRunLength() - 2, stat);
		Assert.assertTrue("Check if results returns True", same);
		System.out.println("Value is true");
	}
}
