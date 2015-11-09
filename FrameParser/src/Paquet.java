import java.util.ArrayList;
import java.util.Collections;


public class Paquet {

	private ArrayList<Message> messages;
	private int identification;
	private boolean haveLastMessage = false;

	public Paquet(int identification)
	{
		this.identification = identification;
		messages = new ArrayList<Message>();
	}

	public Paquet addMessage(Message msg)
	{
		if(msg.isLastMessage())
			this.haveLastMessage = true;

		this.messages.add(msg);

		return this;
		/*
		if(this.haveLastMessage)
		{
			Collections.sort(messages);
			this.attemptReconstruction();
		}
		 */

	}

	public boolean isPackageComplete() {
		if(this.haveLastMessage = true)
		{
			Collections.sort(messages);
			int endIndex = 0;
			for(int i =0; i<this.messages.size(); i++)
			{
				if(this.messages.get(i).isLastMessage())
				{
					endIndex = this.messages.get(i).getEndIndex();
					break;
				}
			}

			int nextEndIndex = endIndex;
			for(int i =0; i<this.messages.size(); i++)
			{
				System.out.println("RECONSTRUCT EndIndex:" + this.messages.get(i).getEndIndex());
				System.out.println("***MSG***" + this.messages.get(i).getMessage());
				if(this.messages.get(i).getEndIndex() == nextEndIndex)
				{
					nextEndIndex = this.messages.get(i).getStartIndex();
					if(nextEndIndex == 0){
						System.out.println("Fully Reconstructed");
						return true;
					}
				}
				else
				{
					System.out.println("RECONSTRUCT FAILED");
					return false;
				}

			}
			return false;
		}
		else
			return false;
	}

	public String reassemblePackage()
	{
		String result = "";
		for(int i =0; i<this.messages.size(); i++)
		{
			result =  this.messages.get(i).getMessage() + result ;
		}
		return result;	
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}
	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}
	public int getIdentification() {
		return identification;
	}
	public void setIdentification(int identification) {
		this.identification = identification;
	}


}
