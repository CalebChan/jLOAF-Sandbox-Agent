package oracle.lfo;

import oracle.Config;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.FixedSequenceExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.FixedSequenceExpert;

public class LfOFixedSequenceTest extends LfOAbstractCreatureTest{

	private static final String PREGEN_TEST_NAME = "FixedSequenceAgent";
	
	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Fixed Sequence";
	}

	@Override
	protected void initSetting() {
		super.init(new FixedSequenceExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new FixedSequenceExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}

}
