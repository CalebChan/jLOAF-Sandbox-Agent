package oracle.lfo;

import java.util.Random;

import oracle.Config;

import org.jLOAF.casebase.CaseBase;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.ZigZagExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.ZigZagExpert;

public class LfOZigZagTest extends LfOAbstractTest{

	@BeforeClass 
	public static void init() throws Exception{
		LfOAbstractTest.init(new ZigZagExpertStrategy(), getPreGenTestName());
	}

	@AfterClass
	public static void cleanUp() throws Exception{
		LfOAbstractTest.cleanUp();
	}
	
	@Before
	public void setUp() throws Exception {
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new ZigZagExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		super.setUp(testAgent, creature);
	}
	
	@Test
	public void testExpert(){
		System.out.println("+++++++++++++++Test Zig Zag Simulation+++++++++++++++");
		Random r = new Random(0);
		for (int i = 0; i < Config.DEFAULT_NUM_OF_SIMULATIONS - 1; i++){
			oracle.runSimulation(true, Config.DEBUG_PRINT_STATS);
			Creature creature = new DirtBasedCreature(r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, Direction.values()[r.nextInt(Direction.values().length)]);
			oracle.setCreature(creature);
			
			CaseBase cb = loo.get(testNo).getTraining();
			SandboxAgent agent = new SandboxAgent(cb, true, Config.DEFAULT_K);
			oracle.setAgent(agent);
			testNo++;
			
			oracle.setTestData(loo.get(testNo).getTesting());
			if (Config.DEBUG_PRINT_STATS){
				System.out.println("-----------------------------------------------");
			}
		}
		oracle.runSimulation(true, Config.DEBUG_PRINT_STATS);
		System.out.println("Average Accuracy : " + oracle.getGlobalAccuracyAvg());
		System.out.println("+++++++++++++++End Test Zig Zag Simulation+++++++++++++++\n\n");
	}

	protected static String getPreGenTestName() {
		return "ZigZagAgent";
	}

}
