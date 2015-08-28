package oracle;

import org.jLOAF.action.Action;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;

import sandbox.Environment;
import sandbox.MovementAction;
import sandbox.sensor.Sensor;
import agent.AbstractSandboxAgent;
import agent.SandboxAction;

public class SandboxOracle extends JLOAFOracle{

	private Environment sandbox;
	protected AbstractSandboxAgent testAgent;
	
	public SandboxOracle(AbstractSandboxAgent ta, RunAgent a, String worldFile) {
		super(a);
				
		this.testAgent = ta;
		buildEnvironment(worldFile);
	}
	
	public void buildEnvironment(String filename){
		
	}
	
	public void runSimulation(boolean learn){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		for (int i = 0; i < Config.DEFAULT_LENGTH; i++){
			sandbox.updateSensor(this.testAgent.getCreature());
			Sensor s = this.testAgent.getCreature().getSensor();
			Input input = convertSensorToInput(s);
			
			MovementAction correctMovementAction = this.testAgent.testAction(this.testAgent.getCreature());
			Action correctAction = new SandboxAction(correctMovementAction);
			Case c = new Case(input, correctAction);
			Action guessAction = stat.senseEnvironment(c);
			SandboxAction sandboxAction = (SandboxAction)guessAction;
			
			sandbox.makeMove(MovementAction.values()[(int) sandboxAction.getFeature().getValue()], this.testAgent.getCreature());
		}
		collectStats(stat);
	}
	
	private Input convertSensorToInput(Sensor s){
		if (s.getSenseKeys().size() == 1){
			Input input = null;
			for (String key : s.getSenseKeys()){
				input = new AtomicInput(key, new Feature((double) s.getSense(key).getValue()));
			}
			return input;
		}else{
			ComplexInput input = new ComplexInput(common.Config.COMPLEX_INPUT_NAME);
			for (String key : s.getSenseKeys()){
				input.add(new AtomicInput(key, new Feature((double) s.getSense(key).getValue())));
			}
		}
		return null;
	}

//	@Override
//	protected String getStatsString(StatisticsWrapper stat){
//		String s = "Creature : " + sandbox.getCreature().get(creatureId).toString() + "\n";
//		StatisticsBundle bundle = stat.getStatisticsBundle();
//		String[] labels = bundle.getLabels();
//		for (int i = 0; i < labels.length; i++){
//			s += labels[i] + " : " + bundle.getAllStatistics()[i] + "\n";
//			if (labels[i].contains("Recall") || labels[i].contains("Classification Accuracy")){
//				s += "\n";
//			}
//		}
//		return s;
//	}

}
