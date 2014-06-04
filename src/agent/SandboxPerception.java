package agent;
import org.jLOAF.Perception;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;

import sandbox.Creature;


public class SandboxPerception implements Perception{

	public Input sense(Creature creature) {
		SandboxInput input = new SandboxInput();
		
		boolean hasTouched = (boolean)creature.getSensor().getSense(StateBasedAgentSenseConfig.TOUCH).getValue();
		double sonar = (double)creature.getSensor().getSense(StateBasedAgentSenseConfig.SONAR).getValue();
		int sound = (int)creature.getSensor().getSense(StateBasedAgentSenseConfig.SOUND).getValue();
		
		input.add(new SandboxFeatureInput("Touch", new Feature((hasTouched)? 1.0 : 0.0)));
		input.add(new SandboxFeatureInput("Sonar", new Feature(sonar)));
		input.add(new SandboxFeatureInput("Sound", new Feature(sound)));
		return input;
	}
}
