package agent.lfo;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;

import common.Config;

import sandbox.Direction;
import sandbox.MovementAction;
import agent.ExpertTraceParser;
import agent.SandboxAction;
import agent.backtracking.SandboxFeatureInput;

public class LfOTraceParser extends ExpertTraceParser{

	public LfOTraceParser(String filename) {
		super(filename);
	}

	@Override
	public Case parseLine(String[] tokens, Case c) {
		ComplexInput ci = new ComplexInput(Config.COMPLEX_INPUT_NAME);
		int index = 0;
		for (Direction d : Direction.values()){
			int type = (int) Double.parseDouble(tokens[index]);
			index++;
			int dist = (int) Double.parseDouble(tokens[index]);
			index++;
			AtomicInput ait = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX, new Feature(type));
			AtomicInput aid = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX, new Feature(dist));
			ci.add(ait);
			ci.add(aid);
		}
		int actionIndex = (int) Double.parseDouble(tokens[index]) - 1;
		MovementAction action = MovementAction.values()[actionIndex];
		SandboxAction a = new SandboxAction(action);
		return new Case(ci, a, c);
	}

}
