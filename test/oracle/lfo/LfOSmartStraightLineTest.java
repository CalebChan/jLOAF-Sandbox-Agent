package oracle.lfo;

import oracle.Config;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.SmartStraightLineExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.SmartStraightLineExpert;

public class LfOSmartStraightLineTest extends LfOAbstractCreatureTest{

	private static final String PREGEN_TEST_NAME = "SmartStraightLineAgent";

	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Straight Line";
	}
	
	@Override
	protected void initSetting() {
		super.init(new SmartStraightLineExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartStraightLineExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}
}
