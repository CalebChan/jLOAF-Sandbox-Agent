package logger;

public class TestLoggerUpdateBundle {
	private String tag;
	
	private String message;

	public TestLoggerUpdateBundle(String tag, String message) {
		this.tag = tag;
		this.message = message;
	}

	public String getTag() {
		return tag;
	}

	public String getMessage() {
		return message;
	}
}
