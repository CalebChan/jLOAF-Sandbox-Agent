package oracle.lfo;

import oracle.Config;
import oracle.SandboxOracle;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.Before;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;
import agent.lfo.SmartRandomExpert;

public class LfOConfigTest {
	
	private SandboxOracle oracle;

	@Before
	public void setUp() throws Exception {
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartRandomExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase("casebase3.cb");
		SandboxAgent agent = new SandboxAgent(cb, true, Config.K_VALUE);
		
		oracle = new SandboxOracle(Config.DEFAULT_WORLD_SIZE, testAgent, 1000, agent, creature, new LfOPerception());
	}

	@Test
	public void testSimuation() {
		System.out.println("+++++++++++++++Test Vanilla Simulation+++++++++++++++");
		oracle.runSimulation(true, true);
		System.out.println("+++++++++++++++End Test Vanilla Simulation+++++++++++++++\n\n");
	}
	
	@Test
	public void testNoLearn(){
		System.out.println("+++++++++++++++Test No Learn Simulation+++++++++++++++");
		oracle.runSimulation(false, true);
		System.out.println("+++++++++++++++End No Learn Simulation+++++++++++++++\n\n");
	}

}
