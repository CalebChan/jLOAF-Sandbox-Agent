package oracle;

import org.jLOAF.agent.RunAgent;

import agent.AbstractSandboxAgent;
import agent.SandboxPerception;

public class SandboxTraceBasedOracle extends JLOAFOracle{
	
	protected SandboxPerception perception;
	protected AbstractSandboxAgent testAgent;
	
	public SandboxTraceBasedOracle(AbstractSandboxAgent ta, RunAgent a, SandboxPerception p) {
		super(a);
		
		this.perception = p;
		this.testAgent = ta;
	}

}
