package jLOAF;

public class kNNRandomTest {

//	private kNNRandom rKNN;
//	
//	@Before
//	public void setUp() throws Exception {
//		SmartRandomExpertStrategy expert = new SmartRandomExpertStrategy();
//		expert.parseFile(Config.DEFAULT_TEST_TRACE_NAME, Config.DEFAULT_TEST_CASEBASE_NAME);
//		CaseBase cb = CaseBaseIO.loadCaseBase(Config.DEFAULT_TEST_CASEBASE_NAME + ".cb");
//		
//		ComplexInput.setClassStrategy(new InputMean());
//		AtomicInput.setClassStrategy(new InputEquality());
//		SandboxFeatureInput.setClassSimilarityMetric(new SandboxSimilarity());
//		
//		rKNN = new kNNRandom(4, cb);
//	}
//	
//	@AfterClass
//	public static void cleanUp() throws Exception{
//		if (!Config.DELETE_TRACE){
//			return;
//		}
//		File f = new File(Config.DEFAULT_TEST_CASEBASE_NAME + common.Config.CASEBASE_EXT);
//		f.delete();
//	}
//
//	@Test
//	public void test() {
//		ComplexInput ci = new ComplexInput("LfoInput");
//		for (Direction d : Direction.values()){
//			int type = 1;
//			int dist = 1;
//			AtomicInput ait = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.TYPE_SUFFIX, new Feature(type));
//			AtomicInput aid = new SandboxFeatureInput(d.name() + DirtBasedAgentSenseConfig.DISTANCE_SUFFIX, new Feature(dist));
//			ci.add(ait);
//			ci.add(aid);
//		}
//		
//		List<Case> cases = rKNN.retrieve(ci);
//		System.out.println("----------------------------------------------------------------------------------");
//		for (Case c : cases){
//			System.out.println(c.toString());
//		}
//	}

}
