package oracle.lfo;

import java.io.File;
import java.util.Random;

import oracle.Config;
import oracle.SandboxOracle;
import oracle.TraceGenerator;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.ExpertStrategy;
import util.expert.lfo.SmartStraightLineExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;
import agent.lfo.SmartStraightLineExpert;

public class LfOSmartStraightLineTest {
	
	private SandboxOracle oracle;
	
	@BeforeClass 
	public static void init() throws Exception{
		Creature c = new DirtBasedCreature(7, 2, Direction.NORTH);
		ExpertStrategy expert = new SmartStraightLineExpertStrategy();
		TraceGenerator.generateTrace(Config.DEFAULT_ITER, Config.DEFAULT_GRID_SIZE, Config.DEFAULT_LENGTH, Config.DEFAULT_TEST_TRACE_NAME, true, c, expert);
		expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
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
		AbstractSandboxAgent testAgent = new SmartStraightLineExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		Assert.assertFalse(cb == null);
		SandboxAgent agent = new SandboxAgent(cb, true, Config.DEFAULT_K);
		
		oracle = new SandboxOracle(Config.DEFAULT_WORLD_SIZE, testAgent, 11, agent, creature, new LfOPerception());
	}
	
	@Test
	public void testExpert(){
		System.out.println("+++++++++++++++Test Smart Straight Line Simulation+++++++++++++++");
		Random r = new Random(0);
		for (int i = 0; i < Config.DEFAULT_NUM_OF_SIMULATIONS - 1; i++){
			oracle.runSimulation(true, true);
			Creature creature = new DirtBasedCreature(r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, Direction.values()[r.nextInt(Direction.values().length)]);
			oracle.setCreature(creature);
			System.out.println("-----------------------------------------------");
		}
		oracle.runSimulation(true, true);
		System.out.println("Average Accuracy : " + oracle.getGlobalAccuracyAvg());
		System.out.println("+++++++++++++++End Test Smart Straight Line Simulation+++++++++++++++\n\n");
	}
}
