package agent.backtracking;

import org.jLOAF.casebase.AtomicCase;
import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Feature;

import agent.ExpertTraceParser;
import agent.SandboxAction;
import sandbox.MovementAction;

public class BackForthAgent extends ExpertTraceParser{
	
	public BackForthAgent(String traceFile){
		super(traceFile);
	}

	@Override
	public Case parseLine(String[] tokens, Case c) {
		int touch = Integer.parseInt(tokens[0]);
		double sonar = Double.parseDouble(tokens[1]);
		
		int sound = Integer.parseInt(tokens[2]);
						
		SandboxInput input = new SandboxInput();
		
		input.add(new SandboxFeatureInput("Touch", new Feature(touch)));
		input.add(new SandboxFeatureInput("Sonar", new Feature(sonar)));
		input.add(new SandboxFeatureInput("Sound", new Feature(sound)));
		
		MovementAction action = MovementAction.valueOf(tokens[3]);
		SandboxAction a = new SandboxAction(action);
		return new AtomicCase(input, a);
	}
}
