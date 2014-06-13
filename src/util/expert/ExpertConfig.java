package util.expert;

import util.expert.backtracking.ActonBasedExpertStrategy;
import util.expert.backtracking.InputBasedExpertStrategy;
import util.expert.lfo.SimpleRandomExpertStrategy;
import util.expert.lfo.SmartStraightLineExpertStrategy;

public class ExpertConfig {

	public static ExpertStrategy STRATEGY[];
	
	private static final int STRATEGY_NUM = 4;
	
	static{
		int count = 0;
		STRATEGY = new ExpertStrategy[STRATEGY_NUM];
		
		STRATEGY[count++] = new ActonBasedExpertStrategy();
		STRATEGY[count++] = new InputBasedExpertStrategy();
		
		STRATEGY[count++] = new SimpleRandomExpertStrategy();
		STRATEGY[count++] = new SmartStraightLineExpertStrategy();
	}

}
