package oracle.lfo.expert;

import oracle.lfo.LfOAbstractCreatureTest;
import util.expert.lfo.SmartRandomExpertStrategy;

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
		setUp();
	}
}
