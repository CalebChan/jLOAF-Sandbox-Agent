
package util.expert.lfo;

import util.expert.LfOExpertStrategy;
import sandbox.Direction;
import sandbox.creature.DirtBasedCreature;
import agent.AbstractSandboxAgent;
import agent.lfo.expert.EqualFixedSequenceExpert;


public class EqualFixedSequenceExpertStrategy extends LfOExpertStrategy{
    
    @Override
    public AbstractSandboxAgent getAgent(int size, int x, int y, Direction dir) {
        return new EqualFixedSequenceExpert(new DirtBasedCreature(x, y, dir), null);
    }

    @Override
    public String getAgentName() {
        return EqualFixedSequenceExpert.class.getSimpleName();
    }
    
}
