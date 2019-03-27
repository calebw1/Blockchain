import java.util.*;
import java.io.*;
import java.sql.*;
public class Block{
	
	private int index; // the index of the block in the list
	private Timestamp timestamp; // time at which transaction has been processed

	private Transaction transaction; // the transaction object
	private String nonce; // random string (for proof of work)
	private String previousHash; // previous hash (in first block, set to string of zeroes of  						         // size of complexity "00000")
	private String hash; // hash of the block(hash of tstring obtained from previous variables via 					// toString()method)

	private long duration;
	private int counter;

	public String toString(){
		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}

	public Block(String index, long timestamp, String previousHash, Transaction transaction, String nonce, String hash){
		this.index = Integer.parseInt(index);
		this.timestamp = new Timestamp(timestamp);
		this.previousHash = previousHash;
		this.transaction = transaction;
		this.hash = hash;
		this.nonce = nonce;
	}

	public Block(Block prev, Transaction transaction){
		this.index = prev.getIndex()+1;
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.previousHash = prev.getHash();
		this.transaction = transaction;
		long start = System.nanoTime();
		int counter = 0;
		while(hash == null){
			this.nonce = findNonce();
			this.hash = findHash();
			counter++;

		}
		long end = System.nanoTime();
		long duration = (end - start);
		this.duration = duration;
		this.counter = counter;
	}

	public String findNonce(){
		StringBuilder s = new StringBuilder();
		for (int i = 0; i<10; i++){
			s.append(rand());
		}
		return s.toString();
	}

	public char rand(){
		Random rand = new Random();
		int random = rand.nextInt((127-33) + 1)+33;
		char charRand = (char)random;
		return charRand;
	}

	public String findHash(){
		String out = null;
		try{
			out = Sha1.hash(this.toString());
		}
		catch(java.io.UnsupportedEncodingException e){};
		if(!out.substring(0,5).equals("00000")){
			return null;
		}
		return out;
	}

	public int getIndex(){
		return index;
	}

	public int getCounter(){
		return counter;
	}

	public long getDuration(){
		return duration;
	}

	public String getNonce(){
		return nonce;
	}

	public String getPreviousHash(){
		return previousHash;
	}

	public String getHash(){
		return hash;
	}

	public Transaction getTransaction(){
		return transaction;
	}

	public long getTimestamp(){
		return timestamp.getTime();
	}

	public void setHash(String hash){
		this.hash = hash;
	}

	public void setNonce(String nonce){
		this.nonce = nonce;
	}	

}