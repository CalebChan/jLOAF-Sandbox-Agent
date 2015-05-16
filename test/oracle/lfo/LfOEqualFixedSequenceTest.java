package oracle.lfo;

import oracle.Config;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.lfo.EqualFixedSequenceExpertStrategy;
import agent.AbstractSandboxAgent;
import agent.lfo.EqualFixedSequenceExpert;

public class LfOEqualFixedSequenceTest extends LfOAbstractCreatureTest{

	public static final String PREGEN_TEST_NAME = "FixedSequenceAgent";

	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Equal Fixed Sequence";
	}

	@Override
	protected void initSetting() {
		super.init(new EqualFixedSequenceExpertStrategy(), PREGEN_TEST_NAME);
		Creature creature = new DirtBasedCreature(7, 2, Direction.NORTH);
		AbstractSandboxAgent testAgent = new EqualFixedSequenceExpert(Config.DEFAULT_WORLD_SIZE, new DirtBasedCreature(creature));
		setUp(testAgent);
	}

}
