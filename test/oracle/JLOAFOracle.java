package oracle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jLOAF.action.Action;
import org.jLOAF.agent.RunAgent;
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
import agent.lfo.DirtBasedAgentSenseConfig;

public class JLOAFOracle {
	protected static String buildPerceptionString(ComplexInput input){
		String s = "";
		
		for (Direction d : Direction.values()){
			int type = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX)).getFeature().getValue();
			int dist = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX)).getFeature().getValue();
			s += type + " " + dist + " ";
		}
		
		return s;
	}
	public static final String LOG_SIM_START = "SIM_START";
	public static final String LOG_SIM_RESULT = "SIM_RESULT";
	public static final String LOG_SIM_END = "SIM_END";
	public static final String LOG_SIM_STAT = "SIM_STAT";
	
	protected JLOAFLogger log;
	
	protected RunAgent agent;
	protected Map<String, Map<String, Integer>> confusionMatrix;
	
	public JLOAFOracle(RunAgent a){
		this.agent = a;
		
		this.confusionMatrix = new HashMap<String, Map<String, Integer>>();
		
		this.log = JLOAFLogger.getInstance();
	}
	
	protected void collectStats(StatisticsWrapper stat){
		mergeConfusionMatrix(stat.getConfusionMatrix());
	}
	
	public double getGlobalAccuracyAvg() {
		ConfusionMatrixStatisticsWrapper w = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		return w.getClassificationAccuracy();
	}
	
	public String getSimulationResults(String agentName){
		String info = "";
		ConfusionMatrixStatisticsWrapper wrapper = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		info += agentName + " Simulation Expected Actions : " + wrapper.getAllExpectedActions().size() + "\n";
		info += agentName + " Simulation Matrix Total : " + getSimulationSize() + "\n";
		for (String actions : wrapper.getAllExpectedActions()){
			info += agentName + " Simulation " + actions + " Percision : " + wrapper.getPrecision(actions) + "\n";
			info += agentName + " Simulation " + actions + " Recall : " + wrapper.getRecall(actions) + "\n";
			info += agentName + " Simulation " + actions + " F1 : " + wrapper.getF1(actions) + "\n";
		}
		info += agentName + " Simulation Matrix Accuracy : " + wrapper.getClassificationAccuracy() + "\n";
		info += agentName + " Simulation Global F1 : " + wrapper.getGlobalF1();
		return info;
	}
	
	public String getSimulationResultsSimple(String agentName){
		String info = "";
		ConfusionMatrixStatisticsWrapper wrapper = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		info += agentName + " Simulation Expected Actions : " + wrapper.getAllExpectedActions().size() + "\n";
		info += agentName + " Simulation Matrix Total : " + getSimulationSize() + "\n";
		info += agentName + " Simulation Matrix Accuracy : " + wrapper.getClassificationAccuracy() + "\n";
		info += agentName + " Simulation Global F1 : " + wrapper.getGlobalF1();
		return info;
	}
	
	public String getConfusionMatrixString(String agentName){
		String s = "";
		String header = "|          |";
		boolean firstIndex = true; 
		HashSet<String> titles = new HashSet<String>();
		titles.addAll(confusionMatrix.keySet());
		for (String s1 : confusionMatrix.keySet()){
			titles.addAll(confusionMatrix.get(s1).keySet());
		}
		for (String s1 : titles){
			s += "|" + String.format("%-10s", s1) + "|";
			for (String s2 : titles){
				if (firstIndex){
					header += String.format("%-10s", s2) + "|";
				}
				if (!confusionMatrix.containsKey(s1) || !confusionMatrix.get(s1).containsKey(s2)){
					s += String.format("%-10d", 0) + "|";
				}else{
					s += String.format("%-10d", confusionMatrix.get(s1).get(s2)) + "|";
				}
			}
			s += "\n";
			firstIndex = false;
		}
		s = s.substring(0, s.length() - 1);
		String label = "|" + String.format("%-" + (header.length() - 2) + "s", agentName + " Confusion Matrix") + "|";
		return label + "\n" + header + "\n" + s;
	}
	
	private int getSimulationSize(){
		int total = 0;
		for (String s1 : confusionMatrix.keySet()){
			for (String s2 : confusionMatrix.get(s1).keySet()){
				total += confusionMatrix.get(s1).get(s2);
			}
		}
		return total;
	}
	
	@Deprecated
	protected String getStatsString(StatisticsWrapper stat){
		return "";
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
	
	public void resetOracleStats(){
		this.confusionMatrix.clear();
	}
	
	public void runSimulation(CaseRun testingData){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		log.logMessage(Level.EXPORT, this.getClass(), LOG_SIM_START,"");
		for (int i = testingData.getRunLength() - 1; i >= 0 ; i--){
			Case c = testingData.getCasePastOffset(i);
			boolean result = testAgent(stat, c);
			if (!result){
				this.agent.learn(c);
			}
		}
		collectStats(stat);
	}

	public void setAgent(RunAgent agent){
		this.agent = agent;
	}
	
	public boolean testAgent(StatisticsWrapper stat, Case testCase){
		Action correctAction = testCase.getAction();
		
		Action guessAction = stat.senseEnvironment(testCase);
//		log.logMessage(Level.EXPORT, this.getClass(), LOG_SIM_RESULT, buildPerceptionString(ci) + move.ordinal());
		return correctAction.equals(guessAction);
	}
}
