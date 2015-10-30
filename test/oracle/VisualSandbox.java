package oracle;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import oracle.lfo.TestConfiguration;

import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.action.ComplexAction;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.AtomicCase;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.jLOAF.reasoning.BacktrackingReasoning;
import org.jLOAF.reasoning.BestRunReasoning;
import org.jLOAF.retrieve.sequence.weight.LinearWeightFunction;
import org.jLOAF.sim.atomic.ActionEquality;
import org.jLOAF.sim.atomic.InputEquality;
import org.jLOAF.sim.complex.ActionMean;
import org.jLOAF.sim.complex.InputMean;
import org.jLOAF.tools.LeaveOneOut;
import org.jLOAF.tools.TestingTrainingPair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.backtracking.SandboxFeatureInput;
import agent.backtracking.SandboxSimilarity;
import sandbox.BoxObstacle;
import sandbox.Creature;
import sandbox.Direction;
import sandbox.Environment;
import sandbox.MovementAction;
import sandbox.creature.DirtBasedCreature;
import sandbox.gui.EnvironmentPanel;
import sandbox.sensor.Sensor;
import util.expert.ExpertStrategy;
import util.expert.lfo.SmartStraightLineExpertStrategy;

public class VisualSandbox {
	
	private Environment sandbox;
	
	private Environment agentSandbox;
	private Creature agentCreature;
	
	protected AbstractSandboxAgent testAgent;
	
	private RunAgent agent;
	
	private boolean splitView;
	
	private JFrame frame;
	
	public static void main(String args[]){
		
		int RUN_NUMBER = 2;
		
		ExpertStrategy expertStrat = new SmartStraightLineExpertStrategy();
		AbstractSandboxAgent expert = expertStrat.getAgent(0, 0, 0, Direction.NORTH);
		
		expertStrat.parseFile("C:/Users/calebchan/Desktop/Stuff/workspace/Test Data/Batch Test 3/TB/Expert/Run " + RUN_NUMBER + "/SmartStraightLineAgent", Config.DEFAULT_TEST_CASEBASE_NAME);
		LeaveOneOut l = LeaveOneOut.loadTrainAndTest(Config.DEFAULT_TEST_CASEBASE_NAME + common.Config.CASEBASE_EXT, Config.DEFAULT_LENGTH, Config.DEFAULT_NUM_OF_SIMULATIONS);
		List<TestingTrainingPair> loo = l.getTestingAndTrainingSets();
		
		CaseBase cb = loo.get(0).getTraining();
		
		//BacktrackingReasoning r = new SequentialReasoning(cb, null, 4, false);
		BacktrackingReasoning r = new BestRunReasoning(cb, 0.75, new LinearWeightFunction(0.1));
		RunAgent agent = new RunAgent(r, cb);
		r.setCurrentRun(agent.getCurrentRun());
		
		ComplexInput.setClassStrategy(new InputMean());
		AtomicInput.setClassStrategy(new InputEquality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		AtomicAction.setClassStrategy(new ActionEquality());
		ComplexAction.setClassStrategy(new ActionMean());
		
		VisualSandbox box = new VisualSandbox(expert, agent, TestConfiguration.MAP_LOCATION[RUN_NUMBER - 1], true);
		box.runSimulation(1000);
	}
	
	
	public VisualSandbox(AbstractSandboxAgent ta, RunAgent a, String worldFile, boolean splitView) {
		this.agent = a;
				
		this.testAgent = ta;
		buildEnvironment(worldFile);
		this.splitView = splitView;
		
		agentSandbox = new Environment(sandbox);
		agentCreature = new DirtBasedCreature(ta.getCreature());
		agentSandbox.addCreature(agentCreature);
		
		buildView(splitView);
	}
	
	private void buildView(boolean splitView){
		frame = new JFrame("Expert | Agent | Time : -1");
		
		frame.setSize(1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new EnvironmentPanel(sandbox), new EnvironmentPanel(agentSandbox));
		split.setDividerLocation(600);
		frame.add(split);
		frame.setVisible(true);
	}
	
	public void runSimulation(int maxIter){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
		for (int i = 0; i < maxIter; i++){
			sandbox.updateSensor(this.testAgent.getCreature());
			Sensor s = this.testAgent.getCreature().getSensor();
			Input input = convertSensorToInput(s);
			
			MovementAction correctMovementAction = this.testAgent.testAction(this.testAgent.getCreature());
			Action correctAction = new SandboxAction(correctMovementAction);
			Case c = new AtomicCase(input, correctAction);
			Action guessAction = stat.senseEnvironment(c);
			SandboxAction sandboxAction = (SandboxAction)guessAction;
			System.out.println("Guess : " + guessAction.toString() + ", Actual : " + correctAction.toString());
			if (!splitView){
				sandbox.makeMove(MovementAction.values()[(int) sandboxAction.getFeature().getValue()], this.testAgent.getCreature());
			}else{
				sandbox.makeMove(MovementAction.values()[(int) ((SandboxAction) correctAction).getFeature().getValue()], this.testAgent.getCreature());
				agentSandbox.makeMove(MovementAction.values()[(int) sandboxAction.getFeature().getValue()], agentCreature);
			}
			try {
				Thread.sleep(1000);
				frame.setTitle("Expert | Agent | Time : " + i);
				frame.repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Input convertSensorToInput(Sensor s){
		if (s.getSenseKeys().size() == 1){
			Input input = null;
			for (String key : s.getSenseKeys()){
				int value = (int) s.getSense(key).getValue();
				input = new SandboxFeatureInput(key, new Feature(value * 1.0));
			}
			return input;
		}else{
			ComplexInput input = new ComplexInput(common.Config.COMPLEX_INPUT_NAME);
			for (String key : s.getSenseKeys()){
				int value = (int) s.getSense(key).getValue();
				input.add(new SandboxFeatureInput(key, new Feature(value * 1.0)));
			}
			return input;
		}
	}
	
	
	private void buildSandbox(Element element){
		int x = Integer.parseInt(element.getAttribute("dx"));
		int y = Integer.parseInt(element.getAttribute("dy"));
		sandbox = new Environment(x, y);
		this.testAgent.setEnvironment(sandbox);
	}
	
	private void buildObjects(Element element){
		NodeList nl = element.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++){
			if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element obj = (Element) nl.item(i);
				String objType = obj.getNodeName();
				int x = (int) Math.floor(Double.parseDouble(obj.getElementsByTagName("x").item(0).getTextContent()));
				int y = (int) Math.floor(Double.parseDouble(obj.getElementsByTagName("y").item(0).getTextContent()));
				if (objType.equals("dirt")){
					sandbox.addDirt(x, y);
					continue;
				}else if (objType.equals("vacuum")){
					this.testAgent.getCreature().moveCreature(x, y, Direction.NORTH);
					continue;
				}
				NodeList shapeList = obj.getElementsByTagName("shape");
				for (int j = 0; j < shapeList.getLength(); j++){
					if (shapeList.item(j).getNodeType() == Node.ELEMENT_NODE){
						Element shape = (Element) shapeList.item(j).getChildNodes().item(0);
						int dx = (int) Math.floor(Double.parseDouble(shape.getAttribute("dx"))) / 2;
						int dy = (int) Math.floor(Double.parseDouble(shape.getAttribute("dx"))) / 2;
						sandbox.addObstacle(new BoxObstacle(x, y, dx, dy));
					}
				}
			}
		}
	}
	
	public void buildEnvironment(String filename){
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(new File(filename));
			doc.getDocumentElement().normalize();
			NodeList e = doc.getChildNodes().item(0).getChildNodes();
			for (int i = 0; i < e.getLength(); i++){
				if (e.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) e.item(i);
					switch (element.getNodeName()){
					case "map":
						buildSandbox(element);
						break;
					case "objects":
						buildObjects(element);
						break;
					default:
						break;
					}
					
				}
			}
			
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
