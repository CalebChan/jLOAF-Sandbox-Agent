package oracle;

import org.jLOAF.action.Action;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;

import sandbox.Creature;
import sandbox.MovementAction;
import sandbox.Sandbox;
import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.SandboxPerception;

public class SandboxOracle extends JLOAFOracle{

	private Sandbox sandbox;
	private int creatureId;
	
	private int worldSize;
	
	protected SandboxPerception perception;
	protected AbstractSandboxAgent testAgent;
	
	public SandboxOracle(AbstractSandboxAgent ta, RunAgent a, SandboxPerception p, int worldSize, Creature creature) {
		super(a);
		
		this.worldSize = worldSize;
		setCreature(creature);
		
		this.perception = p;
		this.testAgent = ta;
	}
	
	public void setCreature(Creature c){
		this.sandbox = new Sandbox(this.worldSize);
		this.creatureId = sandbox.addCreature(c);
		
		sandbox.init();
	}
	
	public void runSimulation(boolean learn){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		for (int i = 0; i < Config.DEFAULT_LENGTH; i++){
			Input in = perception.sense(sandbox.getCreature().get(creatureId));
			MovementAction action = testAgent.testAction(sandbox.getCreature().get(creatureId));
			SandboxAction a = new SandboxAction(action);
			
			Case correctCase = new Case(in, a, null);
			Action act = stat.senseEnvironment(correctCase);
			SandboxAction sa = (SandboxAction)act;
			MovementAction move = MovementAction.values()[(int) sa.getFeature().getValue()];
			
			if (!a.equals(move)){
				this.agent.learn(new Case(in, act, null));
			}
		}
	}

	@Override
	protected String getStatsString(StatisticsWrapper stat){
		String s = "Creature : " + sandbox.getCreature().get(creatureId).toString() + "\n";
		StatisticsBundle bundle = stat.getStatisticsBundle();
		String[] labels = bundle.getLabels();
		for (int i = 0; i < labels.length; i++){
			s += labels[i] + " : " + bundle.getAllStatistics()[i] + "\n";
			if (labels[i].contains("Recall") || labels[i].contains("Classification Accuracy")){
				s += "\n";
			}
		}
		return s;
	}

}
