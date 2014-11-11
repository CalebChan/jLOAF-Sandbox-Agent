package logger.handler;

import java.util.Observable;

import logger.LoggerHandlerIf;
import logger.LoggerUpdateBundle;

public class DebugLoggerHandler implements LoggerHandlerIf{

	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof LoggerUpdateBundle)){
			return;
		}
		LoggerUpdateBundle bundle = (LoggerUpdateBundle)arg1;
		switch (bundle.getTag()){
		case DEBUG_STATS:
			handleMessage(bundle.getMessage(), null);
			break;
		default:
			break;
		
		}
	}

	@Override
	public void handleMessage(String message, String msgOptions) {
		System.out.println("DEBUG :: " + message);
	}

}
