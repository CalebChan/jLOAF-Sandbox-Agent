package agent.backtracking;
import org.jLOAF.MotorControl;
import org.jLOAF.action.Action;
import org.jLOAF.inputs.Feature;

import sandbox.MovementAction;


public class SandboxMotorControl extends MotorControl{

	@Override
	public String control(Action a) {
		if (a instanceof SandboxAction){
			Feature f = ((SandboxAction)a).getFeatures().get(0);
			MovementAction action = MovementAction.values()[(int)f.getValue()];
			return action.name();
		}
		return null;
	}

}
