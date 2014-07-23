
package util.expert.lfo;

import util.expert.LfOExpertStrategy;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import agent.AbstractSandboxAgent;
import agent.lfo.EqualFixedSequenceExpert;


public class EqualFixedSequenceExpertStrategy extends LfOExpertStrategy{
    
    @Override
    public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
        return new EqualFixedSequenceExpert(size, new DirtBasedCreature(x, y, dir));
    }

    @Override
    public String getAgentName() {
        return EqualFixedSequenceExpert.class.getSimpleName();
    }
    
}
