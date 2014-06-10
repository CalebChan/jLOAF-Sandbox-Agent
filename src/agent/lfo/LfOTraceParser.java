package agent.lfo;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;

import sandbox.Direction;
import sandbox.MovementAction;
import agent.ExpertTraceParser;
import agent.SandboxAction;

public class LfOTraceParser extends ExpertTraceParser{

	public LfOTraceParser(String filename) {
		super(filename);
	}

	@Override
	public Case parseLine(String[] tokens, Case c) {
		ComplexInput ci = new ComplexInput("LfoInput");
		int index = 0;
		for (Direction d : Direction.values()){
			int dist = Integer.parseInt(tokens[index]);
			index++;
			int type = Integer.parseInt(tokens[index]);
			index++;
			AtomicInput ait = new AtomicInput(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX, new Feature(type));
			AtomicInput aid = new AtomicInput(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX, new Feature(dist));
			ci.add(ait);
			ci.add(aid);
		}
		MovementAction action = MovementAction.valueOf(tokens[index]);
		SandboxAction a = new SandboxAction(action);
		return new Case(ci, a, c);
	}

}
