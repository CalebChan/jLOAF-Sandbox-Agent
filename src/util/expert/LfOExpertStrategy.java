package util.expert;

import oracle.Config;
import agent.lfo.LfOTraceParser;

public abstract class LfOExpertStrategy implements ExpertStrategy{
	@Override
	public void parseFile(String saveLocation, String filename){
		LfOTraceParser lfo = new LfOTraceParser(saveLocation + Config.TRACE_EXT);
		if (lfo.parseFile()){
			lfo.saveCaseBase(filename + Config.CASEBASE_EXT);
			//System.out.println("DONE CONVERSION");
		}
	}
}
