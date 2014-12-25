package oracle;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.performance.StatisticsWrapper;

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
	
	public SandboxOracle(AbstractSandboxAgent ta, Agent a, SandboxPerception p, int worldSize, Creature creature) {
		super(ta, a, p);
		
		this.worldSize = worldSize;
		setCreature(creature);
	}
	
	public void setCreature(Creature c){
		this.sandbox = new Sandbox(this.worldSize);
		this.creatureId = sandbox.addCreature(c);
		
		sandbox.init();
	}
	
	@Override
	public void setTestData(CaseRun test){
		this.testingData = null;
	}
	
	@Override
	public boolean testAgent(StatisticsWrapper stat, int time){
		Input in = perception.sense(sandbox.getCreature().get(creatureId));
		MovementAction action = testAgent.testAction(sandbox.getCreature().get(creatureId));
		SandboxAction a = new SandboxAction(action);
		
		Case correctCase = new Case(in, a, null);
		
		
		Action act = stat.senseEnvironment(correctCase);
		SandboxAction sa = (SandboxAction)act;
		MovementAction move = MovementAction.values()[(int) sa.getFeature().getValue()];
		
		return a.equals(move);
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
