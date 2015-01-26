package oracle.lfo.test.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.jLOAF.util.JLOAFLogger;
import org.jLOAF.util.JLOAFLogger.JLOAFLoggerInfoBundle;
import org.jLOAF.util.JLOAFLogger.Level;
import org.json.JSONArray;
import org.json.JSONObject;

public class TestSuiteJSONLog implements Observer{

	private FileWriter writer;
	
	private JSONObject output;
	private JSONArray rHistory;
	
	private int time;
	private String runName;
	private double sim;
	private String rType;
	private String action;
	private int testNo;
	
	public TestSuiteJSONLog(String saveFile) {
		this.output = new JSONObject();
		this.rHistory = new JSONArray();
		
		try {
			System.out.println("Write");
			writer = new FileWriter(saveFile);
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof JLOAFLoggerInfoBundle){
			JLOAFLoggerInfoBundle bundle = (JLOAFLoggerInfoBundle)arg1;
			if (bundle.getLevel().equals(Level.EXPORT) && bundle.getTag().equals(JLOAFLogger.JSON_TAG)){
				if (bundle.getMessage().equals("Test")){
					if (this.output.length() > 0){
//						System.out.println("Test Run : " + bundle.getMessageExtra().toString());
						outputToJSON(bundle.getMessageExtra().toString());
					}
					output = new JSONObject();
				}else if (bundle.getMessage().equals("Training") || bundle.getMessage().equals("Testing")){
					output.put(bundle.getMessage(), bundle.getMessageExtra());
				}else if (bundle.getMessage().equals("Time")){
					this.time = (int)bundle.getMessageExtra();
				}else if (bundle.getMessage().equals("RType")){
					this.rType = bundle.getMessageExtra().toString();
				}else if (bundle.getMessage().equals("Run Name")){
					this.runName = bundle.getMessageExtra().toString();
				}else if (bundle.getMessage().equals("Sim")){
					this.sim = (double)bundle.getMessageExtra();
					
					JSONObject rObject = new JSONObject();
					rObject.put("Time", time);
					rObject.put("R Type", rType);
					rObject.put("Name", runName);
					rObject.put("Sim", sim);
					
//					if (runName.equals("3") && testNo == 35){
//						System.out.println("Name : " + rObject.toString());
//					}
					
					this.rHistory.put(rObject);
					
					time = -1;
					rType = "";
					runName = "";
					sim = -1;
					
				}else if (bundle.getMessage().equals("End Retrieval")){
					this.action = bundle.getMessageExtra().toString();
				}else if (bundle.getMessage().equals("Start Retrieval")){
					this.rHistory = null;
					this.rHistory = new JSONArray();
				}else if (bundle.getMessage().equals("Test Start")){
					this.testNo = (int) bundle.getMessageExtra();
				}else if (bundle.getMessage().equals("Result")){
					boolean b = (boolean)bundle.getMessageExtra();
					if (!b){
						JSONObject oo = new JSONObject();
						oo.put("Action", action);
						oo.put("History", this.rHistory);
						
						output.put("" + testNo, oo);
					}
				}
			}
		}
	}
	
	private void outputToJSON(String testNo){
		try {
			writer.write(output.toString() + "\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
