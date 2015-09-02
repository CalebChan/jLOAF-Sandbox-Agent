package util.expert.lfo;

import agent.AbstractSandboxAgent;
import sandbox.Direction;
import util.expert.LfOExpertStrategy;
import sandbox.creature.DirtBasedCreature;
import agent.lfo.expert.ZigZagExpert;

public class ZigZagExpertStrategy extends LfOExpertStrategy{
    
    @Override
    public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
        return new ZigZagExpert(new DirtBasedCreature(x, y, dir), null);
    }

    @Override
    public String getAgentName() {
        return ZigZagExpert.class.getSimpleName();
    }
    
}
