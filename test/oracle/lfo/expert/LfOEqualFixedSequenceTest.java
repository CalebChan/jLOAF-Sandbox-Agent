package oracle.lfo.expert;

import oracle.lfo.LfOAbstractCreatureTest;
import util.expert.lfo.EqualFixedSequenceExpertStrategy;

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
		setUp();
	}

}
