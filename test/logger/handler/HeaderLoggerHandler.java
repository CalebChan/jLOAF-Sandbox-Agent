package logger.handler;

import java.util.Observable;

import logger.LoggerHandlerIf;
import logger.LoggerUpdateBundle;

public class HeaderLoggerHandler implements LoggerHandlerIf{

	public static final String OPEN_TAG = "OPEN";
	public static final String ACCURACY_TAG = "ACCURACY";
	public static final String CLOSE_TAG = "CLOSE";
	
	@Override
	public void update(Observable arg0, Object arg1) {
		if (!(arg1 instanceof LoggerUpdateBundle)){
			return;
		}
		LoggerUpdateBundle bundle = (LoggerUpdateBundle)arg1;
		switch (bundle.getTag()){
		case CONSOLE_HEADER:
			handleMessage(bundle.getMessage(), bundle.getMsgOptions());
			break;
		default:
			break;
		}
	}

	@Override
	public void handleMessage(String message, String msgOptions) {
		switch(msgOptions){
		case OPEN_TAG:
			System.out.println("+++++++++++++++Test " + message +  " Simulation+++++++++++++++");
			break;
		case CLOSE_TAG:
			System.out.println("+++++++++++++++End Test " + message + " Simulation+++++++++++++++");
			break;
		case ACCURACY_TAG:
			System.out.println("Average Accuracy : " + message);
			break;
		default:
			break;
		}
	}

}
