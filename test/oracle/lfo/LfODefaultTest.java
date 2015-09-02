package oracle.lfo;

import oracle.JLOAFOracle;

import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;
import org.junit.Assert;

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

	@Override
	protected void setUp() {
		CaseBase cb = loo.get(testNo).getTraining();
		Assert.assertFalse(cb == null);
		RunAgent agent = createAgent(cb);
		oracle = new JLOAFOracle(agent);
	}

}
