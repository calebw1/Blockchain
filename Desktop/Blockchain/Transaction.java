import java.util.*;

public class Transaction {
	private String sender;
	private String receiver;
	private int amount;

	public String toString(){
		return sender + ":" + receiver + "=" + amount;
	}

	public Transaction(String sender, String receiver, String amount){
		this.sender = sender;
		this.receiver = receiver;
		this.amount = Integer.parseInt(amount);
	}
	
	public String getSender(){
		return sender;
	}

	public String getReceiver(){
		return receiver;
	}

	public int getAmount(){
		return amount;
	}
}