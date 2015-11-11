
public class Message implements Comparable  {

	private int fragmentOffset;
	private int length;
	private int identification;
	private String message;
	private boolean lastMessage;
	private int endIndex;
	private int startIndex;
	
	public Message(int fragmentOffset, int length, int identification, String message, boolean lastMessage)
	{
		this.fragmentOffset = fragmentOffset;
		this.length = length;
		this.identification = identification;
		this.message = message;
		this.lastMessage = lastMessage;
		this.endIndex = (fragmentOffset * 8) + ((length - 20));
		this.startIndex = (fragmentOffset * 8);
	}
	
	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public boolean isLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(boolean lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getFragmentOffset() {
		return fragmentOffset;
	}
	public void setFragmentOffset(int fragmentOffset) {
		this.fragmentOffset = fragmentOffset;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getIdentification() {
		return identification;
	}
	public void setIdentification(int identification) {
		this.identification = identification;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


	public int compareTo(Object o) {
		Message msg = (Message)o;
		int endIndex = msg.getEndIndex();
		return endIndex - this.endIndex;
	}
	
	
	
}
