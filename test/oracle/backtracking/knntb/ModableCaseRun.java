package oracle.backtracking.knntb;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseRun;

public class ModableCaseRun extends CaseRun {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5994651358402675027L;
	
	public ModableCaseRun(CaseRun run){
		for (int i = 0; i < run.getRunLength(); i++){
			this.addCaseToRun(run.getCase(i));
		}
	}
	
	public Case removeCase(int time){
		Case c = this.run.remove(time);
		if (time == 0){
			this.run.get(time).setPreviousCase(null);
		}else if (time < this.run.size()){
			this.run.get(time).setPreviousCase(this.run.get(time - 1));
		}
		return c;
	}

}
