package oracle.lfo;

import oracle.Config;
import oracle.TraceGenerator;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.ParameterNameEnum;
import util.expert.ExpertStrategy;

public abstract class LfOAbstractCreatureTest extends LfOAbstractTest{

	protected void init(ExpertStrategy expert, String testName){
		Creature c = new DirtBasedCreature(7, 2, Direction.NORTH);
		if (!Config.USE_PREGEN_TRACE){
			TraceGenerator.generateTrace(Config.DEFAULT_ITER, Config.DEFAULT_GRID_SIZE, Config.DEFAULT_LENGTH, Config.DEFAULT_TEST_TRACE_NAME, true, c, expert);
			expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
		}else{
			expert.parseFile(list.getStringParam(ParameterNameEnum.TRACE_FOLDER.name()) + "\\" + testName, Config.DEFAULT_TEST_CASEBASE_NAME);
		}
		
		initTrainingAndTestingSets(Config.DEFAULT_TEST_CASEBASE_NAME, Config.DEFAULT_LENGTH, Config.DEFAULT_NUM_OF_SIMULATIONS);
	}
	
	protected abstract String getPreGenTestName();
	protected abstract String getOutputTestName();
	
	protected abstract void initSetting();
}
