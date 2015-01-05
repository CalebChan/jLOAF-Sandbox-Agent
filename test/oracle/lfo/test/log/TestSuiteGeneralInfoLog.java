package oracle.lfo.test.log;

import java.util.Observable;
import java.util.Observer;

import oracle.lfo.LfOAbstractTest;

import org.jLOAF.util.JLOAFLogger.JLOAFLoggerInfoBundle;
import org.jLOAF.util.JLOAFLogger.Level;

public class TestSuiteGeneralInfoLog implements Observer{

	
	@Override
	public void update(Observable o, Object arg) {
		JLOAFLoggerInfoBundle bundle = (JLOAFLoggerInfoBundle)arg;
		
		if (bundle.getLevel().equals(Level.INFO) && bundle.getTag().equals(LfOAbstractTest.LOG_TEST_RESULT)){
			System.out.println(bundle.getMessage());
		}
	}

}
