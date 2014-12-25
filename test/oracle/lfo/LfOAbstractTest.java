package oracle.lfo;

import java.io.File;
import java.util.List;

import oracle.Config;
import oracle.JLOAFOracle;
import oracle.TraceGenerator;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.retrieve.AbstractWeightedSequenceRetrieval;
import org.jLOAF.tools.LeaveOneOut;
import org.jLOAF.tools.TestingTrainingPair;
import org.jLOAF.util.CaseLogger;
import org.jLOAF.util.JLOAFLogger;
import org.jLOAF.util.JLOAFLogger.Level;
import org.junit.Assert;

import agent.AbstractSandboxAgent;
import agent.SandboxAgent;
import agent.lfo.LfOPerception;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.ParameterList;
import util.ParameterNameEnum;
import util.expert.ExpertStrategy;

public abstract class LfOAbstractTest {

	protected JLOAFOracle oracle;
	
	protected static List<TestingTrainingPair> loo;
	protected static int testNo;
	
	protected static ParameterList list;
	
	protected JLOAFLogger log = JLOAFLogger.getInstance();
	
	public static void setParamters(ParameterList list){
		LfOAbstractTest.list = list;
	}
	
	public static final String LOG_TEST_HEADER = "Test Header";
	public static final String LOG_TEST_RESULT = "Test Result";
	public static final String LOG_SIM_STAT = "Log Run";
	
	protected abstract String getPreGenTestName();
	protected abstract String getOutputTestName();
	
	protected static void init(ExpertStrategy expert, String testName) throws Exception{
		Creature c = new DirtBasedCreature(7, 2, Direction.NORTH);
		if (!Config.USE_PREGEN_TRACE){
			TraceGenerator.generateTrace(Config.DEFAULT_ITER, Config.DEFAULT_GRID_SIZE, Config.DEFAULT_LENGTH, Config.DEFAULT_TEST_TRACE_NAME, true, c, expert);
			expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
		}else{
			expert.parseFile(list.getStringParam(ParameterNameEnum.TRACE_FOLDER.name()) + "\\" + testName, Config.DEFAULT_TEST_CASEBASE_NAME);
		}
		LeaveOneOut l = LeaveOneOut.loadTrainAndTest(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT, Config.DEFAULT_LENGTH, Config.DEFAULT_NUM_OF_SIMULATIONS);
		loo = l.getTestingAndTrainingSets();
		testNo = 0;
	}
	
	protected static void cleanUp() throws Exception{
		if (!Config.DELETE_TRACE){
			return;
		}
		File f = new File(Config.DEFAULT_TEST_TRACE_NAME + Config.TRACE_EXT);
		f.delete();
		
		f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		f.delete();
	}
	
	protected SandboxAgent createAgent(CaseBase cb){
		int kValue = list.getIntParam(ParameterNameEnum.K_VALUE.name());
		boolean randomKNN = list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name());
		SequentialReasoning r = null;
		if (list.containsParam(ParameterNameEnum.REASONING.name()) && list.getParam(ParameterNameEnum.REASONING.name()) != null){
			AbstractWeightedSequenceRetrieval retrieval = (AbstractWeightedSequenceRetrieval)list.getParam(ParameterNameEnum.REASONING.name());
			r = new SequentialReasoning(cb, null, kValue, randomKNN, retrieval);
		}else{
			r = new SequentialReasoning(cb, null, kValue, randomKNN);
		}
		
		SandboxAgent agent = new SandboxAgent(cb, r);
		r.setCurrentRun(agent.getCaseRun());
		
		return agent;
	}
	
	protected void setUp(AbstractSandboxAgent testAgent, Creature creature) throws Exception {
		CaseBase cb = loo.get(testNo).getTraining();
		Assert.assertFalse(cb == null);
//		SandboxAgent agent = new SandboxAgent(cb, true, list.getIntParam(ParameterNameEnum.K_VALUE.name()), list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name()));
		SandboxAgent agent = createAgent(cb);
		
		oracle = new JLOAFOracle(testAgent, agent, new LfOPerception());
		oracle.setTestData(loo.get(testNo).getTesting());
	}

	protected void testRun() {
		log.logMessage(Level.INFO, getClass(), LOG_TEST_HEADER, getOutputTestName());
		oracle.resetOracleStats();
		for (int i = 0; i < Config.DEFAULT_NUM_OF_SIMULATIONS - 1; i++){
			if (Config.LOG_RUN){
				CaseLogger.createLogger(true, "LOG_" + getPreGenTestName() + "_" + (i + 1) + "_k_" + list.getIntParam(ParameterNameEnum.K_VALUE.name()) + ".txt");
			}
			log.logMessage(Level.INFO, getClass(), LOG_SIM_STAT, "Run " + getPreGenTestName() + " : " + (i + 1));			
			oracle.runSimulation(Config.AGENT_LEARN);
			//Creature creature = new DirtBasedCreature(r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, r.nextInt(Config.DEFAULT_WORLD_SIZE - 2) + 1, Direction.values()[r.nextInt(Direction.values().length)]);
			//oracle.setCreature(creature);
			
			CaseBase cb = loo.get(testNo).getTraining();
//			SandboxAgent agent = new SandboxAgent(cb, true, list.getIntParam(ParameterNameEnum.K_VALUE.name()), list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name()));
			SandboxAgent agent = createAgent(cb);
			
			oracle.setAgent(agent);
			testNo++;
			
			oracle.setTestData(loo.get(testNo).getTesting());
			log.logMessage(Level.DEBUG, getClass(), LOG_SIM_STAT, "-----------------------------------------------");
		}
		if (Config.LOG_RUN){
			CaseLogger.createLogger(true, "LOG_" + getPreGenTestName() + "_" + (Config.DEFAULT_NUM_OF_SIMULATIONS) + "_k_" + list.getIntParam(ParameterNameEnum.K_VALUE.name()) + ".txt");
		}
		log.logMessage(Level.INFO, getClass(), LOG_SIM_STAT, "Run " + getPreGenTestName() + " : " + (Config.DEFAULT_NUM_OF_SIMULATIONS + 1));
		oracle.runSimulation(Config.AGENT_LEARN);
		log.logMessage(Level.INFO, getClass(), LOG_TEST_RESULT, getOutputTestName() + JLOAFLogger.DEFAULT_DELIMITER + oracle.getGlobalAccuracyAvg());
	}
}
