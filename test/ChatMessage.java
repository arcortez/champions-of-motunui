import java.io.*;

public class ChatMessage implements Serializable {
	protected static final long serialVersionUID = 1122222200L;
	private String message;

	ChatMessage(String message) {
		this.message = message;
	}

	String getMessage() {
		return message;
	}
}
