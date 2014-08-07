package oracle.lfo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	LfOEqualFixedSequenceTest.class, 
	LfOFixedSequenceTest.class,
	LfOSmartRandomTest.class,
	LfOSmartStraightLineTest.class,
	LfOZigZagTest.class, 
	LfOSmartExplorerTest.class })
public class LfOTestSuite {

}
