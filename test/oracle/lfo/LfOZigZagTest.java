package oracle.lfo;

import util.expert.lfo.ZigZagExpertStrategy;

public class LfOZigZagTest extends LfOAbstractCreatureTest{

	private static final String PREGEN_TEST_NAME = "ZigZagAgent";
 
	@Override
	protected void initSetting(){
		super.init(new ZigZagExpertStrategy(), PREGEN_TEST_NAME);
		setUp();
	}
		
	@Override
	protected String getPreGenTestName() {
		return PREGEN_TEST_NAME;
	}

	@Override
	protected String getOutputTestName() {
		return "Zig Zag";
	}
}
