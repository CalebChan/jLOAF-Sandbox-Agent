package agent;
import org.jLOAF.Agent;
import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.action.ComplexAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.atomic.ActionEquality;
import org.jLOAF.sim.atomic.InputEquality;
import org.jLOAF.sim.complex.ActionMean;
import org.jLOAF.sim.complex.InputMean;

import agent.backtracking.SandboxFeatureInput;
import agent.backtracking.BacktrackingPerception;
import agent.backtracking.SandboxSimilarity;

public class SandboxAgent extends Agent{
	protected CaseRun curRun;
	
	private CaseRun agentDecisions;
	
	public SandboxAgent(CaseBase cb, Reasoning reasoning){
		super(null, null, null, cb);
		
		this.curRun = new CaseRun("Current Run");
		this.agentDecisions = new CaseRun("Agent Decision");
		
		ComplexInput.setClassStrategy(new InputMean());
		AtomicInput.setClassStrategy(new InputEquality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		AtomicAction.setClassStrategy(new ActionEquality());
		ComplexAction.setClassStrategy(new ActionMean());
		
		this.r = reasoning;
		this.mc = new SandboxMotorControl();
		this.p = new BacktrackingPerception();
		
		this.cb = cb;
	}	
	
	public CaseRun getCaseRun(){
		return this.curRun;
	}
	
	public CaseRun getAgentDecisions(){
		return this.agentDecisions;
	}

	@Override
	public Action senseEnvironment(Input input) {
		Case curCase = new Case(input, null, curRun.getCurrentCase());
		curRun.addCaseToRun(curCase);
		this.agentDecisions.addCaseToRun(curCase);
		Action a = this.r.selectAction(input);
		curCase.setAction(a);
		return a;
	}
	
	@Override
	public void learn(Case newCase){
		if (newCase == null){
			throw new IllegalArgumentException("Case to Learn is NULL");
		}
		Case c = new Case(newCase.getInput(), newCase.getAction(), this.curRun.getCurrentCase().getPreviousCase());
		this.curRun.amendCurrentCase(c);
		super.learn(c);
	}

}
