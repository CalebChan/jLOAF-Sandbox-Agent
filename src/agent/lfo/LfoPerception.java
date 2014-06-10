package agent.lfo;

import org.jLOAF.Perception;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.sensor.Sensor;

public class LfoPerception implements Perception{

	public Input sense(Creature creature){
		ComplexInput ci = new ComplexInput("LfoInput");
		Sensor s = creature.getSensor();
		for (Direction d : Direction.values()){
			int type = (int) s.getSense(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX).getValue();
			int dist = (int) s.getSense(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX).getValue();
			AtomicInput ait = new AtomicInput(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX, new Feature(type));
			AtomicInput aid = new AtomicInput(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX, new Feature(dist));
			ci.add(ait);
			ci.add(aid);
		}
		return ci;
	}
}
