package oracle.backtracking.knntb;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.reasoning.SequentialReasoning;

import agent.SandboxAgent;

public class SandboxAgentTestStub extends SandboxAgent{

	private CaseBase baseCaseBase;
	
	public SandboxAgentTestStub(CaseBase cb, boolean useSequential, int kValue, boolean useRandomKnn, double problemThreshold) {
		super(cb, useSequential, kValue, useRandomKnn);
		baseCaseBase = new CaseBase();
		for (Case c : cb.getCases()){
			baseCaseBase.add(c);
		}
	}
	
	public void setCaseRun(CaseRun run, int kValue, boolean useRandomKnn, double problemThreshold){
		this.curRun = run;
		this.r = new SequentialReasoning(cb, curRun, kValue, useRandomKnn,problemThreshold, 0.0);
	}
	
	public void setCaseRun(CaseBase cb, int kValue, boolean useRandomKnn, double problemThreshold){
		CaseRun r = new CaseRun();
		Case pastCase = null;
		for (Case c : cb.getCases()){
			c.setPreviousCase(pastCase);
			r.addCaseToRun(c);
			pastCase = c;
		}
		this.setCaseRun(r, kValue, useRandomKnn, problemThreshold);
	}

	
	public void resetAgent(){
		this.cb = new CaseBase();
		this.curRun = new CaseRun();
		for (Case c : baseCaseBase.getCases()){
			cb.add(c);
		}
	}
}
