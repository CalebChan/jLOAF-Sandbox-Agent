package agent;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.inputs.Feature;

import sandbox.MovementAction;


public class SandboxAction extends AtomicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SandboxAction(MovementAction action) {
		super(action.name());
		super.addFeature(new Feature(action.ordinal()));
	}

}
