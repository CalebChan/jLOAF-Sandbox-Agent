package oracle.lfo;

import java.io.File;
import java.util.List;

import oracle.Config;
import oracle.SandboxOracle;
import oracle.TraceGenerator;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.tools.LeaveOneOut;
import org.jLOAF.tools.TestingTrainingPair;
import org.junit.Assert;

import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.ExpertStrategy;

public abstract class LfOAbstractTest {

	protected SandboxOracle oracle;
	
	protected static List<TestingTrainingPair> loo;
	protected static int testNo;
	
	protected static void init(ExpertStrategy expert) throws Exception{
		Creature c = new DirtBasedCreature(7, 2, Direction.NORTH);
		TraceGenerator.generateTrace(Config.DEFAULT_ITER, Config.DEFAULT_GRID_SIZE, Config.DEFAULT_LENGTH, Config.DEFAULT_TEST_TRACE_NAME, true, c, expert);
		expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
		
		LeaveOneOut l = LeaveOneOut.loadTrainAndTest(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT, Config.DEFAULT_LENGTH, Config.DEFAULT_NUM_OF_SIMULATIONS);
		loo = l.getTestingAndTrainingSets();
		testNo = 0;
	}
	
	protected static void cleanUp() throws Exception{
		if (!Config.DELETE_TRACE){
			return;
		}
		File f = new File(Config.DEFAULT_TEST_TRACE_NAME + Config.TRACE_EXT);
		f.delete();
		
		f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		f.delete();
	}
	
	protected void setUp(AbstractSandboxAgent testAgent, Creature creature) throws Exception {
		CaseBase cb = loo.get(testNo).getTraining();
		Assert.assertFalse(cb == null);
		SandboxAgent agent = new SandboxAgent(cb, true, Config.DEFAULT_K);
		
		oracle = new SandboxOracle(Config.DEFAULT_WORLD_SIZE, testAgent, agent, creature, new LfOPerception());
		oracle.setTestData(loo.get(testNo).getTesting());
	}
}
