package util.expert.backtracking;

import agent.AbstractSandboxAgent;
import agent.backtracking.ActionBasedAgent;
import sandbox.Direction;
import sandbox.creature.StateBasedCreature;
import util.expert.BacktrackingExpertStrategy;

public class ActonBasedExpertStrategy extends BacktrackingExpertStrategy{
	@Override
	public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
		return new ActionBasedAgent(size, new StateBasedCreature(x, y, dir));
	}

	@Override
	public String getAgentName() {
		return ActionBasedAgent.class.getSimpleName();
	}

}
