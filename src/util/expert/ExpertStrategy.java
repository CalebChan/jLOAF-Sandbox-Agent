package util.expert;

import sandbox.Direction;
import agent.AbstractSandboxAgent;

public interface ExpertStrategy {
	public void parseFile(String saveLocation, String filename);	
	public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir);
	public String getAgentName();
}
