package oracle.lfo;


import org.jLOAF.retrieve.kNNUtil;
import org.jLOAF.retrieve.sequence.weight.WeightFunction;

import oracle.Config;
import util.ParameterList;
import util.ParameterNameEnum;

public class LFOTestRunner {

	private ParameterList list;
	
	public LFOTestRunner(ParameterList list){
		this.list = list;
	}
	
	public void setParameterList(ParameterList list){
		this.list = list;
	}
	
	public void testAll(LfOAbstractCreatureTest tests[]){
		for (LfOAbstractCreatureTest t : tests){
			LfOAbstractCreatureTest inst;
			try {
				inst = t.getClass().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
			inst.setParamters(list);
			inst.initSetting();
			inst.testRun();
		}
	}
	
	public static void main(String args[]){
		LFOTestRunner runner = new LFOTestRunner(null);
		for (String r : TestConfiguration.REASONINGS){
			System.out.println("Reasoning Method : " + r);
			for (int i = 0; i < TestConfiguration.MAX_RUNS; i++){
				for (int j = 0; j < TestConfiguration.K_VALUES.length; j++){
						ParameterList list = new ParameterList();
						for (int k = 0; k < TestConfiguration.MAX_REPEATED_RUNS; k++){
							list.addParameter(ParameterNameEnum.ITER_NUM.name(), k + 1);
							list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
							list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
							list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), true);
							list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
							list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ("Random") + "/Run " + (i + 1) + "/" + (k + 1));
							
							list.addParameter(ParameterNameEnum.REASONING.name(), r);
							if (r.equals("SEQ")){
								System.out.println("Random, k : " + TestConfiguration.K_VALUES[j] + " Iter " + i);
								runner.setParameterList(list);
								runner.testAll(TestConfiguration.test);	
							}else{
								for (WeightFunction w : TestConfiguration.WEIGHT_FUNCTION){
									kNNUtil.setWeightFunction(w);
									System.out.println("Random, k : " + TestConfiguration.K_VALUES[j] + " Iter " + i);
									System.out.println("Weight Function : " + w.toString());
									runner.setParameterList(list);
									runner.testAll(TestConfiguration.test);
								}
							}
							System.out.println();
						}
						if (TestConfiguration.USE_NON_RANDOM){
							list.addParameter(ParameterNameEnum.ITER_NUM.name(), 1);
							list.addParameter(ParameterNameEnum.RUN_NUMBER.name(), i + 1);
							list.addParameter(ParameterNameEnum.K_VALUE.name(), TestConfiguration.K_VALUES[j]);
							list.addParameter(ParameterNameEnum.USE_RANDOM_KNN.name(), false);
							list.addParameter(ParameterNameEnum.TRACE_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX + "Expert/Run " + (i + 1));
							list.addParameter(ParameterNameEnum.EXPORT_RUN_FOLDER.name(), Config.DEFAULT_TRACE_FOLDER_PREFIX +  "Agent " + ("NonRandom") + "/Run " + (i + 1) + "/" + (1));
							
							list.addParameter(ParameterNameEnum.REASONING.name(), r);
							if (r.equals("SEQ")){
								System.out.println("Non Random, k : " + TestConfiguration.K_VALUES[j]);
								runner.setParameterList(list);
								runner.testAll(TestConfiguration.test);	
							}else{
								for (WeightFunction w : TestConfiguration.WEIGHT_FUNCTION){
									kNNUtil.setWeightFunction(w);
									System.out.println("Non Random, k : " + TestConfiguration.K_VALUES[j]);
									System.out.println("Weight Function : " + w.toString());
									runner.setParameterList(list);
									runner.testAll(TestConfiguration.test);
								}
							}
							System.out.println();
						}
						
				}
			}
			
		}
	}
}
