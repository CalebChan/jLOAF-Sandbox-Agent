package logger;

import java.util.Observable;

public class Logger extends Observable{
	
	private static Logger INSTANCE;
	
	public static Logger getInstance(){
		if (INSTANCE == null){
			INSTANCE = new Logger();
		}
		return INSTANCE;
	}

	public void addHandler(LoggerHandlerIf handler){
		this.addObserver(handler);
	}
	
	public void removeHandler(LoggerHandlerIf handler){
		this.deleteObserver(handler);
	}
	
	public void logMessage(LoggerTag tag, String message, String msgOptions){
		LoggerUpdateBundle bundle = new LoggerUpdateBundle(tag, message, msgOptions);
		this.setChanged();
		this.notifyObservers(bundle);
	}
}
