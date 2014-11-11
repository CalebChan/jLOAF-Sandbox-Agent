package logger;

import java.util.ArrayList;
import java.util.HashMap;

public class TestLogger {
	private HashMap<String, ArrayList<TestLoggerHandlerIf>> handlerMap;
	
	
	private static TestLogger INSTANCE;
	
	public static TestLogger getInstance(){
		if (INSTANCE == null){
			INSTANCE = new TestLogger();
		}
		return INSTANCE;
	}
	
	private TestLogger(){
		this.handlerMap = new HashMap<String, ArrayList<TestLoggerHandlerIf>>();
	}
	
	public void addHandler(String key, TestLoggerHandlerIf handler){
		if (!this.handlerMap.containsKey(key)){
			this.handlerMap.put(key, new ArrayList<TestLoggerHandlerIf>());
		}
		this.handlerMap.get(key).add(handler);
	}
	
	public void removeHandler(String key, TestLoggerHandlerIf handler){
		if (this.handlerMap.containsKey(key)){
			this.handlerMap.get(key).remove(handler);
		}
	}
	
	public void logMessage(String tag, String message){
		if (this.handlerMap.containsKey(tag)){
			for (TestLoggerHandlerIf handler : this.handlerMap.get(tag)){
				handler.logMessage(message);
			}
		}
	}
}
