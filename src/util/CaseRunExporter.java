package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;

import agent.lfo.DirtBasedAgentSenseConfig;

import sandbox.Direction;

public class CaseRunExporter {
	public static void expertCaseRun(CaseRun run, String file){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for (int i = 0; i < run.getRunLength(); i++){
				Case c = run.getCase(0);
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
