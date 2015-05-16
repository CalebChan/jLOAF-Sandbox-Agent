package oracle.lfo;

import oracle.Config;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.SmartExplorerExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.SmartExplorerExpert;

public class LfOSmartExplorerTest extends LfOAbstractCreatureTest {
	
	private static final String PREGEN_TEST_NAME = "SmartRandomExplorerAgent";

	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Smart Explorer";
	}

	@Override
	protected void initSetting() {
		super.init(new SmartExplorerExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new SmartExplorerExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}
}
