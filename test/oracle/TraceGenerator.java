package oracle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import agent.AbstractSandboxAgent;
import sandbox.Creature;
import sandbox.Direction;
import util.expert.ExpertStrategy;

public class TraceGenerator {

	public static final String DEFAULT_TRACE_EXTENSION = ".trace";
	
	public static void generateTrace(int iter, int size, int length, String saveFile, boolean random, Creature c, ExpertStrategy agent){
		int x = c.getX();
		int y = c.getY();
		Direction d = c.getDir();
		
		Random r = new Random(0);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile + DEFAULT_TRACE_EXTENSION));
			for (int i = 0; i < iter; i++){
				run(x, y, d, "TMP", size, agent, length);
				if (random){
					x = r.nextInt(size - 2) + 1;
					y = r.nextInt(size - 2) + 1;
					d = Direction.values()[r.nextInt(Direction.values().length)];
				}
				BufferedReader reader = new BufferedReader(new FileReader("TMP" + DEFAULT_TRACE_EXTENSION));
				String line = reader.readLine();
				while(line != null){
					writer.append(line + "\n");
					line = reader.readLine();
				}
				reader.close();
			}
			writer.close();
			File f = new File("TMP" + DEFAULT_TRACE_EXTENSION);
			f.delete();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static void run(int x, int y, Direction d, String saveFile, int size, ExpertStrategy agent, int iter){
		AbstractSandboxAgent a = agent.getAgent(size, x, y, d);

		if (a == null){
			return;
		}
		
		if (saveFile == null || saveFile.isEmpty()){
			return;
		}
		a.runAgent(iter);
		a.saveTrace(saveFile + DEFAULT_TRACE_EXTENSION);
	}
}
