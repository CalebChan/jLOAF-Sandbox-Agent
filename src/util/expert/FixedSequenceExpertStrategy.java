package util.expert;

import util.SandboxTraceGUI;
import util.expert.LfOExpertStrategy;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import agent.AbstractSandboxAgent;
import agent.lfo.FixedSequenceExpert;



public class FixedSequenceExpertStrategy extends LfOExpertStrategy{
    
    @Override
    public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
        return new FixedSequenceExpert(size, new DirtBasedCreature(x, y, dir));
    }

    @Override
    public String getAgentName() {
        return FixedSequenceExpert.class.getSimpleName();
    }
    
}
