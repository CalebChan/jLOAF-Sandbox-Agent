package agent;

import org.jLOAF.Perception;
import org.jLOAF.inputs.Input;

import sandbox.Creature;

public interface SandboxPerception extends Perception {
	public Input sense(Creature creature);
}
