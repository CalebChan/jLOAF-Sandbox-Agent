package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.tools.CaseBaseIO;

import agent.SandboxAction;
import agent.SandboxInput;
import sandbox.MovementAction;

public class BackForthAgent {

	private String filename;
	private CaseBase casebase;
	
	public BackForthAgent(String traceFile){
		this.filename = traceFile;
		this.casebase = new CaseBase();
	}
	
	public boolean parseFile(){
		BufferedReader ip = null;
		try {
			ip  = new BufferedReader (new FileReader(filename));
			String line = ip.readLine();
			Case c = null;
			while(line != null){
				if (line.isEmpty()){
					continue;
				}
				String tokens[] = line.split("\\|");
				int touch = Integer.parseInt(tokens[0]);
				double sonar = Double.parseDouble(tokens[1]);
				
				int sound = Integer.parseInt(tokens[2]);
								
				SandboxInput input = new SandboxInput();
				
				input.add(new AtomicInput("Touch", new Feature(touch)));
				input.add(new AtomicInput("Sonar", new Feature(sonar)));
				input.add(new AtomicInput("Sound", new Feature(sound)));
				
				MovementAction action = MovementAction.valueOf(tokens[3]);
				SandboxAction a = new SandboxAction(action);
				c = new Case(input, a, c);
				casebase.add(c);
				
				line = ip.readLine();
			}
			if (ip != null){
				ip.close();
			}
		} catch (IOException e) {
			
			return false;
		}
		return true;
	}
	
	public void saveCaseBase(String filename){
		try {
			CaseBaseIO.saveCaseBase(casebase, filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
