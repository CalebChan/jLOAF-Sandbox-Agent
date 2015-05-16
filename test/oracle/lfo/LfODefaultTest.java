package oracle.lfo;

import util.ParameterList;

public class LfODefaultTest extends LfOAbstractTest{

	private String testName;
	
	public LfODefaultTest(ParameterList list, String testName) {
		super(list);
		this.testName = testName;
	}

	@Override
	protected String getOutputTestName() {
		return testName;
	}

}
