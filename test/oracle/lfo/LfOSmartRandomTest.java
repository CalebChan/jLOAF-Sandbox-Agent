package oracle.lfo;

import java.util.Random;

import oracle.Config;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.util.CaseLogger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.SmartRandomExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.SmartRandomExpert;

public class LfOSmartRandomTest extends LfOAbstractTest{
	
	@BeforeClass 
	public static void init() throws Exception{		
		LfOAbstractTest.init(new SmartRandomExpertStrategy(), getPreGenTestName());
	}

	@AfterClass
	public static void cleanUp() throws Exception{
		LfOAbstractTest.cleanUp();
	}
	
	@Before
	public void setUp() throws Exception {
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartRandomExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		super.setUp(testAgent, creature);
	}
	
	@Test
	public void testExpert(){
		if (Config.PRINT_TEST_HEADERS){
			System.out.println("+++++++++++++++Test Smart Random Simulation+++++++++++++++");
		}
		Random r = new Random();
		oracle.resetOracleStats();
		for (int i = 0; i < Config.DEFAULT_NUM_OF_SIMULATIONS - 1; i++){
			if (Config.LOG_RUN){
				CaseLogger.createLogger(true, "LOG_" + getPreGenTestName() + "_" + (i + 1) + "_k_" + Config.DEFAULT_K + ".txt");
			}
			oracle.runSimulation(Config.AGENT_LEARN, Config.DEBUG_PRINT_STATS, i + 1, getPreGenTestName());
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
		if (Config.LOG_RUN){
			CaseLogger.createLogger(true, "LOG_" + getPreGenTestName() + "_" + (Config.DEFAULT_NUM_OF_SIMULATIONS) + "_k_" + Config.DEFAULT_K + ".txt");
		}
		
		oracle.runSimulation(Config.AGENT_LEARN, Config.DEBUG_PRINT_STATS, Config.DEFAULT_NUM_OF_SIMULATIONS, getPreGenTestName());
		if (Config.PRINT_TEST_HEADERS){
			System.out.println("Average Accuracy : " + oracle.getGlobalAccuracyAvg());
			System.out.println("+++++++++++++++End Test Smart Random Simulation+++++++++++++++");
		}else{
			System.out.println("Smart Random Simulation Average Accuracy : " + oracle.getGlobalAccuracyAvg());
			oracle.printStats("Smart Random Simulation");
		}
	}

	protected static String getPreGenTestName() {
		return "SmartRandomAgent";
	}
}
