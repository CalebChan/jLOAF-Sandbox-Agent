package logger;

import java.util.Observer;

public interface LoggerHandlerIf extends Observer{

	public void handleMessage(String message, String msgOptions);
}
