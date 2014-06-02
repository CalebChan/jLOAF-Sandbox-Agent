package agent;
import org.jLOAF.Perception;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;

import sandbox.Creature;


public class SandboxPerception implements Perception{

	public Input sense(Creature creature) {
		SandboxInput input = new SandboxInput();
		
		boolean hasTouched = creature.isHasTouched();
		input.add(new SandboxFeatureInput("Touch", new Feature((hasTouched)? 1.0 : 0.0)));
		input.add(new SandboxFeatureInput("Sonar", new Feature(creature.getSonar())));
		input.add(new SandboxFeatureInput("Sound", new Feature(creature.getSound())));
		return input;
	}
}
