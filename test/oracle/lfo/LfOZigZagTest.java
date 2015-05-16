package oracle.lfo;

import oracle.Config;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.ZigZagExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.ZigZagExpert;

public class LfOZigZagTest extends LfOAbstractCreatureTest{

	private static final String PREGEN_TEST_NAME = "ZigZagAgent";
 
	@Override
	protected void initSetting(){
		super.init(new ZigZagExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new ZigZagExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}
		
	@Override
	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Zig Zag";
	}
}
