package oracle.lfo;

import java.io.File;

import oracle.Config;
import oracle.SandboxOracle;
import oracle.TraceGenerator;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.ExpertStrategy;
import util.expert.lfo.SmartRandomExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;
import agent.lfo.SmartRandomExpert;

public class LfOFixedSequenceTest {

	private SandboxOracle oracle;
	
	@BeforeClass 
	public static void init() throws Exception{
		Creature c = new DirtBasedCreature(7, 2, Direction.NORTH);
		//ExpertStrategy expert = new SmartRandomExpertStrategy();
		//TraceGenerator.generateTrace(Config.DEFAULT_ITER, Config.DEFAULT_GRID_SIZE, Config.DEFAULT_LENGTH, Config.DEFAULT_TEST_TRACE_NAME, true, c, expert);
		//expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
	}

	@AfterClass
	public static void cleanUp() throws Exception{
		File f = new File(Config.DEFAULT_TEST_TRACE_NAME + Config.TRACE_EXT);
		f.delete();
		
		f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		f.delete();
	}
	
	@Before
	public void setUp() throws Exception {
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		//AbstractSandboxAgent testAgent = new SmartRandomExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		Assert.assertFalse(cb == null);
		SandboxAgent agent = new SandboxAgent(cb, true, Config.DEFAULT_K);
		
		//oracle = new SandboxOracle(Config.DEFAULT_WORLD_SIZE, testAgent, 11, agent, creature, new LfOPerception());
	}

}
