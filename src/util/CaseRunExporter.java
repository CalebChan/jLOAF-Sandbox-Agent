package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.ComplexCase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;

import agent.lfo.DirtBasedAgentSenseConfig;
import sandbox.Direction;

public class CaseRunExporter {
	public static void expertCaseRun(ComplexCase run, String file){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			List<Case> cases = new ArrayList<Case>();
			cases.add(run.getCurrentCase());
			cases.addAll(run.getPastCases());
			
			for (int i = cases.size() - 1; i >= 0; i--){
				Case c = cases.get(i);
				String s = "";
				ComplexInput inputs = (ComplexInput)c.getInput();
				for (Direction d : Direction.values()){
					AtomicInput a1 = (AtomicInput) inputs.get(d + DirtBasedAgentSenseConfig.TYPE_SUFFIX);
					AtomicInput a2 = (AtomicInput) inputs.get(d + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX);
					
					s += a1.getFeature().getValue() + " ";
					s += a2.getFeature().getValue() + " ";
				}
				AtomicAction a = (AtomicAction) c.getAction();
				s += a.getFeature().getValue();
				writer.write(s + "\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
