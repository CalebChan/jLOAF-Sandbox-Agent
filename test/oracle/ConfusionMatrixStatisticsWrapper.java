package oracle;

import java.util.Map;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.inputs.Input;
import org.jLOAF.performance.StatisticsWrapper;
import org.jLOAF.performance.actionestimation.LastActionEstimate;

public class ConfusionMatrixStatisticsWrapper extends StatisticsWrapper {
	
	public ConfusionMatrixStatisticsWrapper(Map<String, Map<String, Integer>> matrix) {
		super(new ConfusionMatrixStatisticsWrapper.DummyAgent(), new LastActionEstimate());
		
		this.m_confusion = matrix;
	}

	@Override
	protected void actionPair(Action fromCase, Action fromAgent) {}
	
	private static class DummyAgent extends Agent{

		public DummyAgent() {
			super(null, null, null, null);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Action senseEnvironment(Input input) {
			return null;
		}
		
	}
}
