package agent;
import org.jLOAF.Agent;
import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.complex.Mean;
import agent.backtracking.SandboxFeatureInput;
import agent.backtracking.BacktrackingPerception;
import agent.backtracking.SandboxSimilarity;
import sandbox.Creature;


public class SandboxAgent extends Agent{
	protected CaseRun curRun;
	
	private CaseRun agentDecisions;
	
	public SandboxAgent(CaseBase cb, Reasoning reasoning){
		super(null, null, null, cb);
		
		this.curRun = new CaseRun();
		this.agentDecisions = new CaseRun();
		
		ComplexInput.setClassStrategy(new Mean());
		AtomicInput.setClassStrategy(new Equality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		
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

	public String senseEnvironment(Creature creature) {
		Input in = ((BacktrackingPerception)this.p).sense(creature);
		return this.mc.control(senseEnvironment(in));
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
		Case c = new Case(newCase.getInput(), newCase.getAction(), this.curRun.getCurrentCase().getPreviousCase());
		this.curRun.amendCurrentCase(c);
		super.learn(c);
	}

}
