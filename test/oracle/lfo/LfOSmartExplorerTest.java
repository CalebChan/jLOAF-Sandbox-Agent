package oracle.lfo;

import util.expert.lfo.SmartExplorerExpertStrategy;

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
		setUp();
	}
}
