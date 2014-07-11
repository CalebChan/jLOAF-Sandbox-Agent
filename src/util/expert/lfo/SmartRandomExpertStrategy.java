package util.expert.lfo;

import agent.AbstractSandboxAgent;
import agent.lfo.SmartRandomExpert;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.LfOExpertStrategy;

public class SmartRandomExpertStrategy extends LfOExpertStrategy {

	@Override
	public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
		return new SmartRandomExpert(size, new DirtBasedCreature(x, y, dir));
	}

	@Override
	public String getAgentName() {
		return SmartRandomExpert.class.getSimpleName();
	}
}
