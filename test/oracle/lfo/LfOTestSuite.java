package oracle.lfo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	LfOSmartRandomTest.class,
	LfOSmartStraightLineTest.class,
	LfOZigZagTest.class, 
	//LfOEqualFixedSequenceTest.class, 
	LfOFixedSequenceTest.class,
	LfOSmartExplorerTest.class })
public class LfOTestSuite {

}
