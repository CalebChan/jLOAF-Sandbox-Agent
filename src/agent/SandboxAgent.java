package agent;
import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.tools.CaseBaseIO;

import sandbox.Creature;
import sandbox.Direction;
import sandbox.MovementAction;
import sandbox.Sandbox;


public class SandboxAgent extends Agent{

	private static final int DEFAULT_WORLD_SIZE = 20;
	
	public static void main(String args[]){
		Sandbox sandbox = new Sandbox(DEFAULT_WORLD_SIZE);
		int id = sandbox.addCreature(new Creature(2, 2, Direction.NORTH));
		
		CaseBase cb = CaseBaseIO.loadCaseBase("casebase.cb");
		
		SandboxAgent agent = new SandboxAgent(cb);
		ActionBasedAgent testAgent = new ActionBasedAgent(DEFAULT_WORLD_SIZE);
		
		int right = 0;
		sandbox.init();
		for (int i = 0; i < 20; i++){
			String action = agent.senseEnvironment(sandbox.getCreature().get(id));
			MovementAction a = MovementAction.valueOf(action);
			MovementAction realAction = testAgent.testAction(sandbox.getCreature().get(id));
			if (a.equals(realAction)){
				right++;
			}else{
				System.out.println("Different Result : " + a.toString() + " Real : " + realAction.toString());
			}
			sandbox.takeAction(id, a);
		}
		System.out.println("Correct : " + right);
	}
	
	private CaseRun curRun;
	
	public SandboxAgent(CaseBase cb) {
		super(null, null, null, cb);
		
		this.curRun = new CaseRun();
		
		ComplexInput.setClassStrategy(new Mean());
		AtomicInput.setClassStrategy(new Equality());
		
		this.mc = new SandboxMotorControl();
		this.p = new SandboxPerception();
		
		//this.r = new SimpleKNN(1, cb);
		this.r = new SequentialReasoning(cb, curRun);
		this.cb = cb;
	}

	public String senseEnvironment(Creature creature) {
		Input in = ((SandboxPerception)this.p).sense(curRun, creature);
		Case curCase = new Case(in, null);
		curRun.addCaseToRun(curCase);
		Action a = this.r.selectAction(in);
		curCase.setAction(a);
		return this.mc.control(a);
	}

	@Override
	public Action senseEnvironment(Input input) {
		// TODO Auto-generated method stub
		return null;
	}

}
