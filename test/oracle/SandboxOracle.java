package oracle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.junit.Assert;

import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.SandboxPerception;
import agent.lfo.DirtBasedAgentSenseConfig;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.MovementAction;
import sandbox.Sandbox;
import util.ParameterList;
import util.ParameterNameEnum;

public class SandboxOracle {

	private Sandbox sandbox;
	private int creatureId;
		
	private AbstractSandboxAgent testAgent;
	
	private Agent agent;
	
	private SandboxPerception perception;
	
	private int simulationCount;
	private double globalAccuracy;
	
	private int worldSize;
	
	private CaseRun testingData;
	
	private Map<String, Map<String, Integer>> confusionMatrix;
	
	private ParameterList list;
	
	public SandboxOracle(int worldSize, AbstractSandboxAgent testAgent, Agent agent, Creature creature, SandboxPerception perception, ParameterList list){
		if (worldSize == -1){
			worldSize = Config.DEFAULT_WORLD_SIZE;
		}
		this.list = list;
		this.worldSize = worldSize;
		this.sandbox = new Sandbox(worldSize);
		this.creatureId = sandbox.addCreature(creature);
		
		this.testAgent = testAgent;
		this.agent = agent;
		
		this.perception = perception;
		
		sandbox.init();
		
		this.simulationCount = 0;
		this.globalAccuracy = 0;
		
		this.confusionMatrix = new HashMap<String, Map<String, Integer>>();
	}
	
	public void setCreature(Creature c){
		this.sandbox = new Sandbox(worldSize);
		this.creatureId = sandbox.addCreature(c);
		sandbox.init();
	}
	
	public void setAgent(Agent agent){
		this.agent = agent;
	}
	
	public Agent getAgent(){
		return agent;
	}
	
	public void setTestData(CaseRun testing){
		this.testingData = testing;
	}
	
	public double getGlobalAccuracyAvg(){
		return this.globalAccuracy / (this.simulationCount * 1.0);
	}
	
	public void resetOracleStats(){
		this.globalAccuracy = 0;
		this.simulationCount = 0;
		this.confusionMatrix.clear();
	}
	
	private void collectStats(StatisticsWrapper stat){
		this.globalAccuracy += stat.getClassificationAccuracy();
		this.simulationCount++;
		
		mergeConfusionMatrix(stat.getConfusionMatrix());
	}
	
	public void printStats(String agentName){
		if (!Config.PRINT_BATCH_INFO){
			return;
		}
		ConfusionMatrixStatisticsWrapper wrapper = new ConfusionMatrixStatisticsWrapper(this.confusionMatrix);
		System.out.println(agentName + " Expected Actions : " + wrapper.getAllExpectedActions().size());
		for (String actions : wrapper.getAllExpectedActions()){
			System.out.println(agentName + " " + actions + " Percision : " + wrapper.getPrecision(actions));
			System.out.println(agentName + " " + actions + " Recall : " + wrapper.getRecall(actions));
			System.out.println(agentName + " " + actions + " F1 : " + wrapper.getF1(actions));
		}
		System.out.println(agentName + " Matrix Accuracy : " + wrapper.getClassificationAccuracy());
		System.out.println(agentName + " Global F1 : " + wrapper.getGlobalF1());
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
					this.confusionMatrix.get(s1).put(s2, new Integer(1));
				}
			}
		}
	}
	
	public void runSimulation(boolean toLearn, boolean printStats, int runNumber, String agentName){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		//System.out.println("Running : " + this.testingData.getRunLength() + " test. Start Time : " + (new Timestamp(System.currentTimeMillis()).toString()));
		
		BufferedWriter writer = null;
		if (Config.EXPORT_RUN) {
				File f = new File(list.getStringParam(ParameterNameEnum.EXPORT_RUN_FOLDER.name()));
				if (!f.exists()){
					f.mkdirs();
				}
				try {
					writer = new BufferedWriter(new FileWriter(list.getStringParam(ParameterNameEnum.EXPORT_RUN_FOLDER.name()) + "\\" + agentName + "_" + runNumber + "_k_" + list.getIntParam(ParameterNameEnum.K_VALUE.name()) + ".txt"));
				} catch (IOException e) {
					Assert.fail();
				}
		}
		
		for (int i = 0; i < this.testingData.getRunLength(); i++){
			Case correctCase = null;
			MovementAction action = null;
			if (this.testingData == null){
				Input in = perception.sense(sandbox.getCreature().get(creatureId));
				action = testAgent.testAction(sandbox.getCreature().get(creatureId));
				SandboxAction a = new SandboxAction(action);
				correctCase = new Case(in, a, null);
			}else{
				correctCase = this.testingData.getCase(i);
				action = MovementAction.values()[(int) ((SandboxAction)correctCase.getAction()).getFeature().getValue()];
			}
			
			
			Action act = stat.senseEnvironment(correctCase);
			SandboxAction sa = (SandboxAction)act;
			MovementAction move = MovementAction.values()[(int) sa.getFeature().getValue()];
			
			if (Config.EXPORT_RUN) {
				String s = buildPerceptionString((ComplexInput) correctCase.getInput());
				try {
					writer.write(s + move.ordinal() + "\n");
				} catch (IOException e) {
					Assert.fail();
				}
			}
			
			if (toLearn){
				if (!action.equals(move)){
					agent.learn(correctCase);
					move = action;
				}
			}
			sandbox.takeAction(creatureId, move);
		}
		
		collectStats(stat);
		
		if (printStats){
			System.out.println("Creature : " + sandbox.getCreature().get(creatureId).toString());
			StatisticsBundle bundle = stat.getStatisticsBundle();
			String[] labels = bundle.getLabels();
			for (int i = 0; i < labels.length; i++){
				System.out.println(labels[i] + " : " + bundle.getAllStatistics()[i]);
				if (labels[i].contains("Recall") || labels[i].contains("Classification Accuracy")){
					System.out.println("");
				}
			}
		}
		this.testingData = null;
		if (Config.EXPORT_RUN){
			try {
				writer.close();
			} catch (IOException e) {
				Assert.fail();
			}
		}
		//System.out.println("End of Test. End Time : " + (new Timestamp(System.currentTimeMillis()).toString()));
	}
	
	private String buildPerceptionString(ComplexInput input){
		String s = "";
		
		for (Direction d : Direction.values()){
			int type = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX)).getFeature().getValue();
			int dist = (int) ((AtomicInput)input.get(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX)).getFeature().getValue();
			s += type + " " + dist + " ";
		}
		
		return s;
	}
	
	public void runSimulation(boolean toLearn, boolean printStats){
		runSimulation(toLearn, printStats, 0, "");
	}
}
