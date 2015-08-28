package util.expert.lfo;

import agent.AbstractSandboxAgent;
import sandbox.Direction;
import util.expert.LfOExpertStrategy;
import sandbox.creature.DirtBasedCreature;
import agent.lfo.SmartExplorerExpert;

public class SmartExplorerExpertStrategy extends LfOExpertStrategy{
    
    @Override
    public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
        return new SmartExplorerExpert(new DirtBasedCreature(x, y, dir), null);
    }

    @Override
    public String getAgentName() {
        return SmartExplorerExpert.class.getSimpleName();
    }
    
}