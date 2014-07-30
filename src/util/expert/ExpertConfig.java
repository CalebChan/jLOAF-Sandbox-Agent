package util.expert;

import util.expert.backtracking.ActonBasedExpertStrategy;
import util.expert.backtracking.InputBasedExpertStrategy;
import util.expert.lfo.SimpleRandomExpertStrategy;
import util.expert.lfo.SmartStraightLineExpertStrategy;
import util.expert.lfo.ZigZagExpertStrategy;
import util.expert.lfo.EqualFixedSequenceExpertStrategy;
import util.expert.lfo.FixedSequenceExpertStrategy;
import util.expert.lfo.SmartExplorerExpertStrategy;

public class ExpertConfig {

	public static ExpertStrategy STRATEGY[];
	
	private static final int STRATEGY_NUM = 8;
	
	static{
		int count = 0;
		STRATEGY = new ExpertStrategy[STRATEGY_NUM];
		
		STRATEGY[count++] = new ActonBasedExpertStrategy();
		STRATEGY[count++] = new InputBasedExpertStrategy();
		
		STRATEGY[count++] = new SimpleRandomExpertStrategy();
		STRATEGY[count++] = new SmartStraightLineExpertStrategy();
                STRATEGY[count++] = new FixedSequenceExpertStrategy();
                STRATEGY[count++] = new EqualFixedSequenceExpertStrategy();
                STRATEGY[count++] = new ZigZagExpertStrategy();
                STRATEGY[count++] = new SmartExplorerExpertStrategy();
	}

}
