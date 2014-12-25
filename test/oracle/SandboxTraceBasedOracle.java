package oracle;

import org.jLOAF.Agent;

import agent.AbstractSandboxAgent;
import agent.SandboxPerception;

public class SandboxTraceBasedOracle extends JLOAFOracle{
	
	public SandboxTraceBasedOracle(AbstractSandboxAgent ta, Agent a, SandboxPerception p) {
		super(ta, a, p);
	}

}
