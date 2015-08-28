package oracle.lfo;

import agent.AbstractSandboxAgent;
import oracle.Config;
import sandbox.Direction;
import sandbox.Environment;
import util.ParameterNameEnum;
import util.expert.ExpertStrategy;

public abstract class LfOAbstractCreatureTest extends LfOAbstractTest{

	protected AbstractSandboxAgent expertAgent;
	
	protected void init(ExpertStrategy expert, String testName){
		
		expertAgent = expert.getAgent(0, 0, 0, Direction.NORTH);
		
		if (Config.USE_PREGEN_TRACE){
//			expert.parseFile(list.getStringParam(ParameterNameEnum.TRACE_FOLDER.name()) + "\\" + testName, Config.DEFAULT_TEST_CASEBASE_NAME);
			expert.parseFile(list.getStringParam(ParameterNameEnum.TRACE_FOLDER.name()) + "/" + testName, Config.DEFAULT_TEST_CASEBASE_NAME);
			initTrainingAndTestingSets(Config.DEFAULT_TEST_CASEBASE_NAME, Config.DEFAULT_LENGTH, Config.DEFAULT_NUM_OF_SIMULATIONS);
		}
	}
	
	public void setupExpertAgent(int x, int y, Direction d, Environment env){
		expertAgent.getCreature().moveCreature(x, y, Direction.NORTH);
		expertAgent.setEnvironment(env);
	}
	
	protected abstract String getPreGenTestName();
	protected abstract String getOutputTestName();
	
	protected abstract void initSetting();
}
