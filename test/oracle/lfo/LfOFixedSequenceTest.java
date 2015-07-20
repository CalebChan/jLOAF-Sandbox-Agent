package oracle.lfo;

import util.expert.lfo.FixedSequenceExpertStrategy;

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
		setUp();
	}

}
