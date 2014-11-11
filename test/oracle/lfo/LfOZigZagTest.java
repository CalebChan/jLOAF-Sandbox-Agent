package oracle.lfo;

import oracle.Config;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.ZigZagExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.ZigZagExpert;

public class LfOZigZagTest extends LfOAbstractTest{
	
	private static final String PREGEN_TEST_NAME = "ZigZagAgent";

	@BeforeClass 
	public static void init() throws Exception{
		LfOAbstractTest.init(new ZigZagExpertStrategy(), PREGEN_TEST_NAME);
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
		testRun();
	}

	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Zig Zag";
	}

}
