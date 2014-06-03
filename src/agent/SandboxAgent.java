package agent;
import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.casebase.CaseRun;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.jLOAF.reasoning.SequentialReasoning;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.tools.CaseBaseIO;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.MovementAction;
import sandbox.Sandbox;


public class SandboxAgent extends Agent{

	private static final int DEFAULT_WORLD_SIZE = 40;
	private static final int DEFAULT_K = 4;
	
	public static void main(String args[]){
		//CaseLogger.createLogger(true, "");
		Sandbox sandbox = new Sandbox(DEFAULT_WORLD_SIZE);
		Creature creature = new Creature(2, 2, Direction.NORTH);
		int id = sandbox.addCreature(new Creature(creature));
		
		ActionBasedAgent testAgent = new ActionBasedAgent(DEFAULT_WORLD_SIZE, new Creature(creature));
		
		CaseBase cb = CaseBaseIO.loadCaseBase("casebase.cb");
		
		SandboxAgent agent = new SandboxAgent(cb, true);
		sandbox.init();
		
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		SandboxPerception percept = new SandboxPerception();
		
		for (int i = 0; i < 100; i++){
			Input in = percept.sense(sandbox.getCreature().get(id));
			MovementAction action = testAgent.testAction(sandbox.getCreature().get(id));
			SandboxAction a = new SandboxAction(action);
			Action act = stat.senseEnvironment(new Case(in, a, null));
			SandboxAction sa = (SandboxAction)act;
			MovementAction move = MovementAction.values()[(int) sa.getFeatures().get(0).getValue()];
			Creature c = sandbox.getCreature().get(id);
			String data = c.isHasTouched() + "|" + c.getSonar() + "|" + c.getSound();
			String local = c.getX() + "|" + c.getY() + "|" + c.getDir();
			System.out.println("Creature : " + data + " Actual Action : " + action + " Agent Action : " + move + " Local : " + local);
			sandbox.takeAction(id, move);
		}
		StatisticsBundle bundle = stat.getStatisticsBundle();
		String[] labels = bundle.getLabels();
		for (int i = 0; i < labels.length; i++){
			System.out.println(labels[i] + " : " + bundle.getAllStatistics()[i]);
			if (labels[i].contains("Recall") || labels[i].contains("Classification Accuracy")){
				System.out.println("");
			}
		}
	}
	
	private CaseRun curRun;
	
	public SandboxAgent(CaseBase cb, boolean useSequential) {
		super(null, null, null, cb);
		
		this.curRun = new CaseRun();
		
		ComplexInput.setClassStrategy(new Mean());
		AtomicInput.setClassStrategy(new Equality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		
		this.mc = new SandboxMotorControl();
		this.p = new SandboxPerception();
		
		if (useSequential){
			this.r = new SequentialReasoning(cb, curRun, DEFAULT_K);
		}else{
			this.r = new SimpleKNN(DEFAULT_K, cb);
		}
		
		this.cb = cb;
	}

	public String senseEnvironment(Creature creature) {
		Input in = ((SandboxPerception)this.p).sense(creature);
		return this.mc.control(senseEnvironment(in));
	}

	@Override
	public Action senseEnvironment(Input input) {
		Case curCase = new Case(input, null, curRun.getCurrentCase());
		curRun.addCaseToRun(curCase);
		Action a = this.r.selectAction(input);
		curCase.setAction(a);
		return a;
	}

}
