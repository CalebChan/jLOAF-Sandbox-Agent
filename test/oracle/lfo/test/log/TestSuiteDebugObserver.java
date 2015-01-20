package oracle.lfo.test.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.jLOAF.util.JLOAFLogger.JLOAFLoggerInfoBundle;
import org.jLOAF.util.JLOAFLogger.Level;

public class TestSuiteDebugObserver implements Observer{

	private BufferedWriter writer;
	
	private int maxTime;
	private int count;
	private int totalTime;
	
	public TestSuiteDebugObserver(String filename) throws IOException{
		writer = new BufferedWriter(new FileWriter(filename));
		maxTime = -1;
		count = 0;
	}
	
	public void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof JLOAFLoggerInfoBundle){
			JLOAFLoggerInfoBundle bundle = (JLOAFLoggerInfoBundle)arg;
			if (bundle.getLevel().equals(Level.DEBUG)){
				if (bundle.getMessage().contains("Action")){
					//writeMessage(bundle, "Earliest State-Action Pair Used : " + maxTime);
					totalTime += maxTime;
					maxTime = -1;
				}if (bundle.getMessage().contains("Candidates")){
					count++;
				}else if (bundle.getTag().equals("S G O")){
					int time = Integer.parseInt(bundle.getMessage());
					maxTime = Math.max(maxTime, time);
				}else if (bundle.getTag().equals("COUNT")){
					writeMessage(bundle, "Test No : " + bundle.getMessage());
					count++;
				}else if (bundle.getTag().equals("END")){
					double avg = totalTime * 1.0 / count;
					writeMessage(bundle, "Average State-Action Pair Used : " + avg);
					count = 0;
					maxTime = -1;
					totalTime = 0;
				}
			}
		}
	}
	
	private void writeMessage(JLOAFLoggerInfoBundle extraInfo, String message){
		String out = "";
		out += "[" + extraInfo.getLevel().toString()  + ":" + extraInfo.getMessageClass().getName()+ "] - ";
		out += "(TAG = " + extraInfo.getTag() + ")";
		out += message;
		try {
			writer.write(out + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}