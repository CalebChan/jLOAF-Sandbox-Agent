package oracle;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
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
	
	private int iterations;
	
	private AbstractSandboxAgent testAgent;
	
	private Agent agent;
	
	private SandboxPerception perception;
	
	private int simulationCount;
	private double globalAccuracy;
	
	private int worldSize;
	
	public SandboxOracle(int worldSize, AbstractSandboxAgent testAgent, int iterations, Agent agent, Creature creature, SandboxPerception perception){
		if (worldSize == -1){
			worldSize = Config.DEFAULT_WORLD_SIZE;
		}
		this.worldSize = worldSize;
		this.sandbox = new Sandbox(worldSize);
		this.creatureId = sandbox.addCreature(creature);
		
		this.iterations = iterations;
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
	
	public double getGlobalAccuracyAvg(){
		return this.globalAccuracy / (this.simulationCount * 1.0);
	}
	
	public void runSimulation(boolean toLearn, boolean printStats){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		
		for (int i = 0; i < this.iterations; i++){
			Input in = perception.sense(sandbox.getCreature().get(creatureId));
			MovementAction action = testAgent.testAction(sandbox.getCreature().get(creatureId));
			SandboxAction a = new SandboxAction(action);
			
			Case correctCase = new Case(in, a, null);
			
			Action act = stat.senseEnvironment(correctCase);
			SandboxAction sa = (SandboxAction)act;
			MovementAction move = MovementAction.values()[(int) sa.getFeature().getValue()];
			
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
	}
}
