package oracle.lfo;

import oracle.Config;
import oracle.SandboxOracle;

import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.Before;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import agent.AbstractSandboxAgent;
import agent.lfo.expert.SmartRandomExpert;

public class LfOConfigTest {
	
	private SandboxOracle oracle;

	@Before
	public void setUp() throws Exception {
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartRandomExpert(new DirtBasedCreature(creature), null);
		
		CaseBase cb = CaseBaseIO.loadCaseBase("casebase3.cb");
//		SandboxAgent agent = new SandboxAgent(cb, true, Config.K_VALUE, Config.DEFAULT_USE_RANDOM_KNN);
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
