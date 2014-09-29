package oracle;

public class Config {

	public static final int DEFAULT_WORLD_SIZE = 10;
	public static final int DEFAULT_K = 4;
	
	public static final boolean USE_RANDOM_KNN = true;
	
	public static final int DEFAULT_MAX_K = 5;
	
	public static final String DEFAULT_CASEBASE_NAME = "ActionCasebase.cb";
	
	public static final String DEFAULT_TEST_CASEBASE_NAME = "TestCasebase";
	public static final String DEFAULT_TEST_TRACE_NAME = "TestTrace";
	public static final int DEFAULT_ITER = 7;
	public static final int DEFAULT_GRID_SIZE = 11;
	public static final int DEFAULT_LENGTH = 1000;
	
	public static final String CASEBASE_EXT = ".cb";
	public static final String TRACE_EXT = ".trace";
	
	public static final String DEFAULT_TRACE_FOLDER = "trace";
	
	public static final boolean EXPORT_RUN = true;
	public static final String DEFAULT_EXPORT_RUN_FOLDER = "run";
	
	public static final String DEFAULT_DELIMITER = " ";
	
	public static final int DEFAULT_NUM_OF_SIMULATIONS = 7;
	
	public static final boolean DEBUG_PRINT_STATS = false;
	
	public static final boolean DELETE_TRACE = true;
	public static final boolean USE_PREGEN_TRACE = true;
	
	public static final boolean LOG_RUN = false;
	
	// The learn flag should always be set to false when using leave-one-out
	public static final boolean AGENT_LEARN = true;
}
