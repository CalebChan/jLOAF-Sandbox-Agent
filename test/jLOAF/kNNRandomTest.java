package jLOAF;

import java.io.File;
import java.util.List;

import oracle.Config;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.retrieve.kNNRandom;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.tools.CaseBaseIO;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import sandbox.Direction;
import util.expert.lfo.SmartRandomExpertStrategy;
import agent.backtracking.SandboxFeatureInput;
import agent.backtracking.SandboxSimilarity;
import agent.lfo.DirtBasedAgentSenseConfig;

public class kNNRandomTest {

	private kNNRandom rKNN;
	
	@Before
	public void setUp() throws Exception {
		SmartRandomExpertStrategy expert = new SmartRandomExpertStrategy();
		expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_TEST_CASEBASE_NAME + ".cb");
		
		ComplexInput.setClassStrategy(new Mean());
		AtomicInput.setClassStrategy(new Equality());
		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
		
		rKNN = new kNNRandom(4, cb);
	}
	
	@AfterClass
	public static void cleanUp() throws Exception{
		if (!Config.DELETE_TRACE){
			return;
		}
		File f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + Config.CASEBASE_EXT);
		f.delete();
	}

	@Test
	public void test() {
		ComplexInput ci = new ComplexInput("LfoInput");
		for (Direction d : Direction.values()){
			int type = 1;
			int dist = 1;
			AtomicInput ait = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX, new Feature(type));
			AtomicInput aid = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX, new Feature(dist));
			ci.add(ait);
			ci.add(aid);
		}
		
		List<Case> cases = rKNN.retrieve(ci);
		System.out.println("----------------------------------------------------------------------------------");
		for (Case c : cases){
			System.out.println(c.toString());
		}
	}

}
