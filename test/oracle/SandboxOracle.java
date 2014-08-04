package oracle;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;

import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.SandboxPerception;
import sandbox.Creature;
import sandbox.MovementAction;
import sandbox.Sandbox;

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
	
	public SandboxOracle(int worldSize, AbstractSandboxAgent testAgent, Agent agent, Creature creature, SandboxPerception perception){
		this(worldSize, testAgent, -1, agent, creature, perception);
	}
	
	public SandboxOracle(int worldSize, AbstractSandboxAgent testAgent, int iter, Agent agent, Creature creature, SandboxPerception perception){
		if (worldSize == -1){
			worldSize = Config.DEFAULT_WORLD_SIZE;
		}
		this.worldSize = worldSize;
		this.sandbox = new Sandbox(worldSize);
		this.creatureId = sandbox.addCreature(creature);
		
		this.testAgent = testAgent;
		this.agent = agent;
		
		this.perception = perception;
		
		sandbox.init();
		
		this.simulationCount = 0;
		this.globalAccuracy = 0;
	}
	
	public void setCreature(Creature c){
		this.sandbox = new Sandbox(worldSize);
		this.creatureId = sandbox.addCreature(c);
		sandbox.init();
	}
	
	public void setAgent(Agent agent){
		this.agent = agent;
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
	}
	
	public void runSimulation(boolean toLearn, boolean printStats){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		
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
			//System.out.println("Correct Action : " + action.name() + " , Returned Action : " + move.name());
			if (toLearn){
				if (!action.equals(move)){
					agent.learn(correctCase);
					move = action;
				}
			}
			sandbox.takeAction(creatureId, move);
		}
		
		this.globalAccuracy += stat.getClassificationAccuracy();
		this.simulationCount++;
		
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
	}
}
