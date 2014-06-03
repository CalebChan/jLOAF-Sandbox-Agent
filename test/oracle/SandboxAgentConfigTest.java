package oracle;

import static org.junit.Assert.*;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.Before;
import org.junit.Test;

import agent.ActionBasedAgent;
import agent.SandboxAgent;
import agent.StateBasedAgent;

import sandbox.Creature;
import sandbox.Direction;

public class SandboxAgentConfigTest {

	private static final int DEFAULT_WORLD_SIZE = 10;
	private static final int DEFAULT_K = 4;
	
	private SandboxOracle oracle;
	
	@Before
	public void setup(){
		Creature creature = new Creature(6, 6, Direction.SOUTH);
		StateBasedAgent testAgent = new ActionBasedAgent(DEFAULT_WORLD_SIZE, new Creature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase("casebase.cb");
		SandboxAgent agent = new SandboxAgent(cb, true, DEFAULT_K);
		
		oracle = new SandboxOracle(DEFAULT_WORLD_SIZE, testAgent, 100, agent, creature);
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
