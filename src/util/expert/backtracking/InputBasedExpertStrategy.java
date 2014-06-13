package util.expert.backtracking;

import agent.AbstractSandboxAgent;
import agent.backtracking.InputBasedAgent;
import sandbox.Direction;
import sandbox.creature.StateBasedCreature;
import util.expert.BacktrackingExpertStrategy;

public class InputBasedExpertStrategy extends BacktrackingExpertStrategy{

	@Override
	public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
		return new InputBasedAgent(size, new StateBasedCreature(x, y, dir));
	}

	@Override
	public String getAgentName() {
		return InputBasedAgent.class.getSimpleName();
	}

}
