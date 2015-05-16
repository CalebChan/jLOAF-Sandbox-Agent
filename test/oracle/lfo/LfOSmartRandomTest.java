package oracle.lfo;

import oracle.Config;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.SmartRandomExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.SmartRandomExpert;

public class LfOSmartRandomTest extends LfOAbstractCreatureTest{

	private static final String PREGEN_TEST_NAME = "SmartRandomAgent";

	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Smart Random";
	}
	
	@Override
	protected void initSetting() {
		super.init(new SmartRandomExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartRandomExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}
}
