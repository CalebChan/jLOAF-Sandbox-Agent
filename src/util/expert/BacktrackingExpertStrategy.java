package util.expert;

import util.SandboxTraceGUI;
import agent.backtracking.BackForthAgent;

public abstract class BacktrackingExpertStrategy implements ExpertStrategy{

	@Override
	public void parseFile(String saveLocation, String filename){
		BackForthAgent bf = new BackForthAgent(saveLocation + SandboxTraceGUI.DEFAULT_TRACE_EXTENSION);
		if (bf.parseFile()){
			bf.saveCaseBase(filename + SandboxTraceGUI.DEFAULT_CASEBASE_EXTENSION);
			System.out.println("DONE");
		}
	}
}
