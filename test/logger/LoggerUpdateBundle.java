package logger;

public class LoggerUpdateBundle {
	private LoggerTag tag;
	
	private String message;
	
	private String msgOptions;

	public LoggerUpdateBundle(LoggerTag tag, String message, String msgOptions) {
		this.tag = tag;
		this.message = message;
		this.msgOptions = msgOptions;
	}
	
	public String getMsgOptions(){
		return msgOptions;
	}

	public LoggerTag getTag() {
		return tag;
	}

	public String getMessage() {
		return message;
	}
}
