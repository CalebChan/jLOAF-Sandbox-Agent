package oracle;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jLOAF.action.Action;
import org.jLOAF.agent.RunAgent;
import org.jLOAF.casebase.AtomicCase;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.ComplexCase;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.ClassificationStatisticsWrapper;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sandbox.BoxObstacle;
import sandbox.Direction;
import sandbox.Environment;
import sandbox.MovementAction;
import sandbox.sensor.Sensor;
import agent.AbstractSandboxAgent;
import agent.SandboxAction;
import agent.backtracking.SandboxFeatureInput;
import agent.lfo.expert.SmartStraightLineExpert;
import agent.lfo.expert.ZigZagExpert;

public class SandboxOracle extends JLOAFOracle{

	private Environment sandbox;
	protected AbstractSandboxAgent testAgent;
	
	public SandboxOracle(AbstractSandboxAgent ta, RunAgent a, String worldFile) {
		super(a);
				
		this.testAgent = ta;
		buildEnvironment(worldFile);
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
//		System.out.println("File name : " + filename);
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
	
	public void runSimulation(boolean toLearn){
		runSimulation(null);
	}
	
	@Override
	public void runSimulation(ComplexCase testingData){
		StatisticsWrapper stat = new ClassificationStatisticsWrapper(agent, new LastActionEstimate());
//		System.out.println("Name : " + this.testAgent.getClass().getSimpleName());
		long systemTime = 0;
		for (int i = 0; i < Config.DEFAULT_LENGTH; i++){
			long tmpTime = System.currentTimeMillis();
			sandbox.updateSensor(this.testAgent.getCreature());
			Sensor s = this.testAgent.getCreature().getSensor();
			Input input = convertSensorToInput(s);
			
			MovementAction correctMovementAction = this.testAgent.testAction(this.testAgent.getCreature());
			if (correctMovementAction.equals(MovementAction.STAND)){
				System.out.println();
			}
			Action correctAction = new SandboxAction(correctMovementAction);
			Case c = new AtomicCase(input, correctAction);
			Action guessAction = stat.senseEnvironment(c);
			SandboxAction sandboxAction = (SandboxAction)guessAction;
			
//			if (i % 100 == 0){
//				System.out.println("Test No : " + i);
//			}
//			System.out.println("Guess : " + guessAction.toString() + ", Actual : " + correctAction.toString());
			boolean move = sandbox.makeMove(MovementAction.values()[(int) sandboxAction.getFeature().getValue()], this.testAgent.getCreature());
			if (this.testAgent instanceof SmartStraightLineExpert){
				SmartStraightLineExpert ee = (SmartStraightLineExpert)this.testAgent;
				ee.resetDirection(Direction.convertActToDir(MovementAction.values()[(int) sandboxAction.getFeature().getValue()]));
			}else if (this.testAgent instanceof ZigZagExpert){
				ZigZagExpert ee = (ZigZagExpert)this.testAgent;
				ee.resetDirection(Direction.convertActToDir(MovementAction.values()[(int) sandboxAction.getFeature().getValue()]));
			}
			systemTime += (System.currentTimeMillis() - tmpTime);
		}
		System.out.println("Run Time : " + (systemTime / (Config.DEFAULT_LENGTH * 1.0)));
		collectStats(stat);
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

//	@Override
//	protected String getStatsString(StatisticsWrapper stat){
//		String s = "Creature : " + sandbox.getCreature().get(creatureId).toString() + "\n";
//		StatisticsBundle bundle = stat.getStatisticsBundle();
//		String[] labels = bundle.getLabels();
//		for (int i = 0; i < labels.length; i++){
//			s += labels[i] + " : " + bundle.getAllStatistics()[i] + "\n";
//			if (labels[i].contains("Recall") || labels[i].contains("Classification Accuracy")){
//				s += "\n";
//			}
//		}
//		return s;
//	}

}
