package agent.backtracking;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;

import agent.SandboxPerception;
import agent.backtracking.StateBasedAgentSenseConfig;

import sandbox.Creature;


public class BacktrackingPerception implements SandboxPerception{

	public Input sense(Creature creature) {
		SandboxInput input = new SandboxInput();
		
		boolean hasTouched = (boolean)creature.getSensor().getSense(StateBasedAgentSenseConfig.TOUCH).getValue();
		double sonar = (double)creature.getSensor().getSense(StateBasedAgentSenseConfig.SONAR).getValue();
		int sound = (int)creature.getSensor().getSense(StateBasedAgentSenseConfig.SOUND).getValue();
		
		input.add(new SandboxFeatureInput(StateBasedAgentSenseConfig.TOUCH, new Feature((hasTouched)? 1.0 : 0.0)));
		input.add(new SandboxFeatureInput(StateBasedAgentSenseConfig.SONAR, new Feature(sonar)));
		input.add(new SandboxFeatureInput(StateBasedAgentSenseConfig.SOUND, new Feature(sound)));
		return input;
	}
}
