package util.expert;

import util.SandboxTraceGUI;
import agent.lfo.LfOTraceParser;

public abstract class LfOExpertStrategy implements ExpertStrategy{
	@Override
	public void parseFile(String saveLocation, String filename){
		LfOTraceParser lfo = new LfOTraceParser(saveLocation + SandboxTraceGUI.DEFAULT_TRACE_EXTENSION);
		if (lfo.parseFile()){
			lfo.saveCaseBase(filename + SandboxTraceGUI.DEFAULT_CASEBASE_EXTENSION);
			System.out.println("DONE");
		}
	}
}
