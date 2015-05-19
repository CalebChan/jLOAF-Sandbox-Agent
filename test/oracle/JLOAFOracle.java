package oracle;

import java.util.HashMap;
import java.util.Map;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.jLOAF.util.JLOAFLogger;
import org.jLOAF.util.JLOAFLogger.Level;

import sandbox.Direction;
import sandbox.MovementAction;
import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.SandboxPerception;
import agent.lfo.DirtBasedAgentSenseConfig;

public class JLOAFOracle {
	public static final String LOG_SIM_START = "SIM_START";
	public static final String LOG_SIM_RESULT = "SIM_RESULT";
	public static final String LOG_SIM_END = "SIM_END";
	public static final String LOG_SIM_STAT = "SIM_STAT";
	protected JLOAFLogger log;
	
	protected AbstractSandboxAgent testAgent;
	protected Agent agent;
	
	protected SandboxPerception perception;
	
	protected int simulationCount;
	protected double globalAccuracy;
	
	protected CaseRun testingData;
	protected Map<String, Map<String, Integer>> confusionMatrix;
	
	public JLOAFOracle(AbstractSandboxAgent ta, Agent a, SandboxPerception p){
		this.testAgent = ta;
		this.agent = a;
		this.perception = p;
		
		this.simulationCount = 0;
		this.globalAccuracy = 0;
		
		this.confusionMatrix = new HashMap<String, Map<String, Integer>>();
		
		this.log = JLOAFLogger.getInstance();
	}
	
	public void setAgent(Agent agent){
		this.agent = agent;
	}
	
	public void setTestData(CaseRun test){
		this.testingData = test;
	}
	
	public void resetOracleStats(){
		this.globalAccuracy = 0;
		this.simulationCount = 0;
		this.confusionMatrix.clear();
	}
	
	private void mergeConfusionMatrix(Map<String, Map<String, Integer>> map1){
		for (String s1 : map1.keySet()){
			if (!this.confusionMatrix.containsKey(s1)){
				this.confusionMatrix.put(s1, new HashMap<String, Integer>());
			}
			for (String s2 : map1.get(s1).keySet()){
				if (this.confusionMatrix.get(s1).containsKey(s2)){
					int i = this.confusionMatrix.get(s1).get(s2).intValue();
					i += map1.get(s1).get(s2).intValue();
					this.confusionMatrix.get(s1).put(s2, new Integer(i));
				}else{
					this.confusionMatrix.get(s1).put(s2, map1.get(s1).get(s2));
				}
			}
		}
	}
	
	protected void printConfusionMatrix(Map<String, Map<String, Integer>> matrix){
		for (String s1 : matrix.keySet()){
			System.out.printf("%-20s", s1);
			for (String s2 : matrix.get(s1).keySet()){
				System.out.printf("%-20s", matrix.get(s1).get(s2).intValue());
			}
			System.out.println("");
		}
	}
	
	protected void collectStats(StatisticsWrapper stat){
		this.globalAccuracy += stat.getClassificationAccuracy();
		this.simulationCount++;
		
		mergeConfusionMatrix(stat.getConfusionMatrix());
	}
	
	protected static String buildPerceptionString(ComplexInput input){
		String s = "";
		
		for (Direction d : Direction.values()){
			int type = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX)).getFeature().getValue();
			int dist = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX)).getFeature().getValue();
			s += type + " " + dist + " ";
		}
		
		return s;
	}
	
	public boolean testAgent(StatisticsWrapper stat, int time){
		Case correctCase = this.testingData.getCasePastOffset(time);
		MovementAction action = MovementAction.values()[(int) ((SandboxAction)correctCase.getAction()).getFeature().getValue()];
		
		Action act = stat.senseEnvironment(correctCase);
		SandboxAction sa = (SandboxAction)act;
		
		MovementAction move = MovementAction.values()[(int) sa.getFeature().getValue()];
		
		ComplexInput ci = (ComplexInput)correctCase.getInput();
		log.logMessage(Level.EXPORT, this.getClass(), LOG_SIM_RESULT, buildPerceptionString(ci) + move.ordinal());
		return action.equals(move);
	}
	
	public void runSimulation(boolean toLearn){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		log.logMessage(Level.EXPORT, this.getClass(), LOG_SIM_START,"");
		for (int i = this.testingData.getRunLength() - 1; i >= 0 ; i--){
//			System.out.println("Test NO : " + i);
//			log.logMessage(Level.EXPORT, getClass(), JLOAFLogger.JSON_TAG, "Test Start", i);
			boolean result = testAgent(stat, i);
//			System.out.println("Result : " + result);
//			log.logMessage(Level.EXPORT, getClass(), JLOAFLogger.JSON_TAG, "Result", result);
			if (toLearn && !result){
				this.agent.learn(this.testingData.getCasePastOffset(i));
			}
		}
		collectStats(stat);
//		log.logMessage(Level.INFO, this.getClass(), LOG_SIM_STAT, getStatsString(stat));
		this.testingData = null;
//		log.logMessage(Level.EXPORT, this.getClass(), LOG_SIM_END,"");
	}
	
	protected String getStatsString(StatisticsWrapper stat){
		return "";
	}
	
	public String getSimulationResults(String agentName){
		String info = "";
		ConfusionMatrixStatisticsWrapper wrapper = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		info += agentName + " Simulation Expected Actions : " + wrapper.getAllExpectedActions().size() + "\n";
		for (String actions : wrapper.getAllExpectedActions()){
			info += agentName + " Simulation " + actions + " Percision : " + wrapper.getPrecision(actions) + "\n";
			info += agentName + " Simulation " + actions + " Recall : " + wrapper.getRecall(actions) + "\n";
			info += agentName + " Simulation " + actions + " F1 : " + wrapper.getF1(actions) + "\n";
		}
		info += agentName + " Simulation Matrix Accuracy : " + wrapper.getClassificationAccuracy() + "\n";
		info += agentName + " Simulation Global F1 : " + wrapper.getGlobalF1();
		return info;
	}

	public double getGlobalAccuracyAvg() {
		ConfusionMatrixStatisticsWrapper w = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		return w.getClassificationAccuracy();
	}
}
