package oracle.backtracking.knntb;

import org.jLOAF.Reasoning;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.CaseBase;

public class SandboxAgentTestStub extends RunAgent{

	public SandboxAgentTestStub(Reasoning reasoning, CaseBase casebase) {
		super(reasoning, casebase);
		// TODO Auto-generated constructor stub
	}

//	private CaseBase baseCaseBase;
//	
//	public SandboxAgentTestStub(CaseBase cb, Reasoning r, double problemThreshold) {
//		super(r, cb);
//		baseCaseBase = new CaseBase();
//		for (Case c : cb.getCases()){
//			baseCaseBase.add(c);
//		}
//	}
//	
//	public void setCaseRun(CaseRun run, int kValue, boolean useRandomKnn, double problemThreshold){
//		this.currentRun = run;
//		this.r = new SequentialReasoning(cb, currentRun, kValue, useRandomKnn,problemThreshold, 0.0);
//	}
//	
//	public void setCaseRun(CaseBase cb, int kValue, boolean useRandomKnn, double problemThreshold){
//		CaseRun r = new CaseRun();
//		Case pastCase = null;
//		for (Case c : cb.getCases()){
//			c.setPreviousCase(pastCase);
//			r.addCaseToRun(c);
//			pastCase = c;
//		}
//		this.setCaseRun(r, kValue, useRandomKnn, problemThreshold);
//	}
//
//	
//	public void resetAgent(){
//		this.cb = new CaseBase();
//		this.currentRun = new CaseRun();
//		for (Case c : baseCaseBase.getCases()){
//			cb.add(c);
//		}
//	}
}
