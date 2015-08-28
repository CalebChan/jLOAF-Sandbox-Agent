package oracle.backtracking;

import java.util.Arrays;
import java.util.Collection;

import oracle.Config;
import oracle.SandboxOracle;

import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.StateBasedCreature;
import agent.AbstractSandboxAgent;
import agent.backtracking.ActionBasedAgent;

@RunWith(Parameterized.class)
public class KNNConfigTest {
	
	private SandboxOracle oracle;
	private int k;
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[Config.DEFAULT_MAX_K][1];
		for (int i = 0; i < Config.DEFAULT_MAX_K; i++){
			data[i][0] = Math.max(i * 2, 1);
		}
		return Arrays.asList(data);
	}

	public KNNConfigTest(int k){
		this.k = k;
	}
	
	
	@Before
	public void setup(){
		Creature creature = new StateBasedCreature(2, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new ActionBasedAgent(Config.DEFAULT_WORLD_SIZE, new StateBasedCreature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_CASEBASE_NAME);
//		SandboxAgent agent = new SandboxAgent(cb, false, k, Config.DEFAULT_USE_RANDOM_KNN);
		RunAgent agent = new RunAgent(new SimpleKNN(k, cb), cb);
		
		oracle = new SandboxOracle(testAgent, agent, "");
	}
	
	@Test
	public void testSimuation() {
		System.out.println("+++++++++++++++Test Vanilla Simulation +++++++++++++++");
		System.out.println("+++++++++++++++        K = " + this.k + " +++++++++++++++");
		oracle.runSimulation(true);
		System.out.println("+++++++++++++++End Test Vanilla Simulation+++++++++++++++\n\n");
	}
	
	@Test
	public void testNoLearn(){
		System.out.println("+++++++++++++++Test No Learn Simulation+++++++++++++++");
		System.out.println("+++++++++++++++        K = " + this.k + " +++++++++++++++");
		oracle.runSimulation(false);
		System.out.println("+++++++++++++++End No Learn Simulation+++++++++++++++\n\n");
	}

}
