package oracle.backtracking;

import oracle.Config;
import oracle.SandboxOracle;

import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.Before;
import org.junit.Test;

import agent.AbstractSandboxAgent;
import agent.backtracking.ActionBasedAgent;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.StateBasedCreature;

public class BacktrackingConfigTest {

	private SandboxOracle oracle;
	
	@Before
	public void setup(){
		Creature creature = new StateBasedCreature(2, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new ActionBasedAgent(Config.DEFAULT_WORLD_SIZE, new StateBasedCreature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_CASEBASE_NAME);
		//SandboxAgent agent = new SandboxAgent(cb, true, Config.K_VALUE, Config.DEFAULT_USE_RANDOM_KNN);
		SequentialReasoning r = new SequentialReasoning(cb, null, Config.K_VALUE, Config.DEFAULT_USE_RANDOM_KNN);
		RunAgent agent = new RunAgent(r, cb);
		r.setCurrentRun(agent.getCurrentRun());
		
		oracle = new SandboxOracle(testAgent, agent, "");
	}
	
	@Test
	public void testSimuation() {
		System.out.println("+++++++++++++++Test Vanilla Simulation+++++++++++++++");
		oracle.runSimulation(true);
		System.out.println("+++++++++++++++End Test Vanilla Simulation+++++++++++++++\n\n");
	}
	
	@Test
	public void testNoLearn(){
		System.out.println("+++++++++++++++Test No Learn Simulation+++++++++++++++");
		oracle.runSimulation(false);
		System.out.println("+++++++++++++++End No Learn Simulation+++++++++++++++\n\n");
	}

}
