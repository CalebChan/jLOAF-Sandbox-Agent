package util.expert.lfo;

import agent.AbstractSandboxAgent;
import agent.lfo.SmartStraightLineExpert;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import util.expert.LfOExpertStrategy;

public class SmartStraightLineExpertStrategy extends LfOExpertStrategy {

	@Override
	public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
		return new SmartStraightLineExpert(size, new DirtBasedCreature(x, y, dir));
	}

	@Override
	public String getAgentName() {
		return SmartStraightLineExpert.class.getSimpleName();
	}

}
