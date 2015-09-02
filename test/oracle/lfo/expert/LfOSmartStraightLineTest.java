package oracle.lfo.expert;

import oracle.lfo.LfOAbstractCreatureTest;
import util.expert.lfo.SmartStraightLineExpertStrategy;

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
		setUp();
	}
}
