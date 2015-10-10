package oracle.lfo;

import java.io.File;
import java.util.List;

import oracle.Config;
import oracle.JLOAFOracle;
import oracle.SandboxOracle;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.action.ComplexAction;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.reasoning.BacktrackingReasoning;
import org.jLOAF.reasoning.BestRunReasoning;
import org.jLOAF.reasoning.KNNBacktracking;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.retrieve.SequenceRetrieval;
import org.jLOAF.sim.atomic.ActionEquality;
import org.jLOAF.sim.atomic.InputEquality;
import org.jLOAF.sim.complex.ActionMean;
import org.jLOAF.sim.complex.InputMean;
import org.jLOAF.tools.LeaveOneOut;
import org.jLOAF.tools.TestingTrainingPair;
import org.jLOAF.util.JLOAFLogger;
import org.jLOAF.util.JLOAFLogger.Level;
import agent.backtracking.SandboxFeatureInput;
import agent.backtracking.SandboxSimilarity;
import util.ParameterList;
import util.ParameterNameEnum;

public abstract class LfOAbstractTest {

	public static final String LOG_TEST_RESULT = "Test Result";
	
	protected JLOAFOracle oracle;
	
	protected List<TestingTrainingPair> loo;
	protected int testNo;
	
	protected ParameterList list;
	
	protected JLOAFLogger log = JLOAFLogger.getInstance();
	
	protected abstract String getOutputTestName();
	
	public LfOAbstractTest(ParameterList list){
		this.list = list;
		testNo = 0;
	}
	
	public LfOAbstractTest(){
		this(null);
	}
	
	protected abstract void setUp();
	
	public void setParamters(ParameterList list){
		this.list = list;
	}
	
	public void initTrainingAndTestingSets(String caseBaseName, int length, int numOfSimulations){
		LeaveOneOut l = LeaveOneOut.loadTrainAndTest(caseBaseName + common.Config.CASEBASE_EXT, length, numOfSimulations);
		loo = l.getTestingAndTrainingSets();
	}
	
	public void cleanUp(){
		if (!Config.DELETE_TRACE){
			return;
		}
		File f = new File(Config.DEFAULT_TEST_TRACE_NAME + common.Config.TRACE_EXT);
		if (f.exists()){
			f.delete();
		}
		
		f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + common.Config.CASEBASE_EXT);
		if (f.exists()){
			f.delete();
		}
	}
	
	protected RunAgent createAgent(CaseBase cb){
		int kValue = list.getIntParam(ParameterNameEnum.K_VALUE.name());
		boolean randomKNN = list.getBoolParam(ParameterNameEnum.USE_RANDOM_KNN.name());
		BacktrackingReasoning r = null;
		
		if (list.containsParam(ParameterNameEnum.REASONING.name()) && list.getParam(ParameterNameEnum.REASONING.name()).equals("KNN")){
			r = new KNNBacktracking(cb, null, kValue, randomKNN, randomKNN);
		}else if (list.containsParam(ParameterNameEnum.REASONING.name()) && list.getParam(ParameterNameEnum.REASONING.name()).equals("BEST")){
			r = new BestRunReasoning(cb, kValue, randomKNN);
		}else if (list.containsParam(ParameterNameEnum.REASONING.name()) && list.getParam(ParameterNameEnum.REASONING.name()).equals("SEQ")){
			if(list.containsParam(ParameterNameEnum.RETRIEVAL.name()) && list.getParam(ParameterNameEnum.RETRIEVAL.name()) != null){
				SequenceRetrieval retrieval = (SequenceRetrieval)list.getParam(ParameterNameEnum.RETRIEVAL.name());
				r = new SequentialReasoning(cb, null, kValue, randomKNN, retrieval);
			}else{
				r = new SequentialReasoning(cb, null, kValue, randomKNN);
			}
		}
		
		RunAgent agent = new RunAgent(r, cb);
		r.setCurrentRun(agent.getCurrentRun());
		
		ComplexInput.setClassStrategy(new InputMean());
		AtomicInput.setClassStrategy(new InputEquality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		AtomicAction.setClassStrategy(new ActionEquality());
		ComplexAction.setClassStrategy(new ActionMean());
		
		return agent;
	}

	protected void testRun() {
		long initTime = System.currentTimeMillis();
		oracle.resetOracleStats();
		
		for (int i = 0; i < Config.DEFAULT_NUM_OF_SIMULATIONS - 1; i++){			
			oracle.runSimulation(loo.get(testNo).getTesting());
			
			testNo++;
			CaseBase cb = loo.get(testNo).getTraining();
			RunAgent agent = createAgent(cb);
			if (oracle instanceof SandboxOracle){
				SandboxOracle o = (SandboxOracle)oracle;
//				System.out.println("Maps No : " + testNo + ", Name : " + TestConfiguration.MAP_LOCATION[testNo]);
				o.buildEnvironment(TestConfiguration.MAP_LOCATION[testNo]);
			}
			oracle.setAgent(agent);
			
		}
		
		oracle.runSimulation(loo.get(testNo).getTesting());
		long endTime = (System.currentTimeMillis() - initTime) / 1000;
		log.logMessage(Level.INFO, getClass(), LOG_TEST_RESULT, getOutputTestName() + " Simulation Average Accuracy : " + oracle.getGlobalAccuracyAvg());
		log.logMessage(Level.INFO, getClass(), LOG_TEST_RESULT, oracle.getSimulationResultsSimple(getOutputTestName()));
		System.out.println(oracle.getSimulationResults(getOutputTestName()));
		System.out.println(oracle.getConfusionMatrixString(getOutputTestName()));
		System.out.println(getOutputTestName() + " Simulation Time : " + endTime);
		
	}
}
