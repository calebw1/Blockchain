import java.util.*;
import java.io.*;
import java.lang.*;
import java.sql.Timestamp;


public class BlockChain{
	private ArrayList<Block> blockchain;

	public BlockChain(ArrayList<Block> blk){
		this.blockchain = blk;
	}

	public static BlockChain fromFile(String filename) throws IOException{
		ArrayList<Block> b = new ArrayList<>();
		Scanner file = new Scanner(new File(filename));
		String index = "";
		String sender = "";
		String receiver = "";
		String previousHash = "00000";
		String hash = "";
		String amount = "";
		String nonce = "";
		long timestamp;

		while(file.hasNextLine()){
			index = file.nextLine();
			timestamp = Long.parseLong(file.nextLine());
			sender = file.nextLine();
			receiver = file.nextLine(); 
			amount = file.nextLine();
			nonce = file.nextLine();
			hash = file.nextLine();

			Transaction trans = new Transaction(sender, receiver, amount);
			Block blk = new Block(index, timestamp, previousHash, trans, nonce, hash);
			b.add(blk);	
			previousHash = hash;
		}

		BlockChain newBlockChain = new BlockChain(b);
		return newBlockChain;
	}


	public int getBalance(String username){
		int balance = 0;
		
		for(int i = 0; i<blockchain.size(); i++){
			if(blockchain.get(i).getTransaction().getSender().equals(username)){
				balance = balance - blockchain.get(i).getTransaction().getAmount();
			}
			if(blockchain.get(i).getTransaction().getReceiver().equals(username)){
				balance = balance + blockchain.get(i).getTransaction().getAmount();
			}
		}
		return balance;
	}

	public boolean validateBlockchain(){

		Block genesis = getBlockChain().get(0);

		if(!genesis.getPreviousHash().equals("00000")){
			System.out.println("Break here 1");
			return false;
		}
		if(!genesis.getHash().equals(genesis.findHash())){
			System.out.println("Break here 2");
			return false;
		}
		if(genesis.getIndex() != 0){
			System.out.println("Break here 3");
			return false;
		}

		for(int i = 1; i < blockchain.size(); i++){
			Block current = blockchain.get(i);
			Block previous = blockchain.get(i-1);

			if(!current.getPreviousHash().equals(previous.getHash())){
				System.out.println("Break here 4");
				return false;
			}
			if(!current.getHash().equals(current.findHash())){
				System.out.println("Break here 5");
				return false;
			}
			if(current.getIndex() != i){
				System.out.println("Break here 6");
				return false;
			}
		}
		return true;
	}

	public void addBlock(Block block){
		blockchain.add(block);
	}

	public ArrayList<Block> getBlockChain(){
		return this.blockchain;
	}

	public void toFile(String fileName) throws FileNotFoundException{
		PrintWriter x = new PrintWriter(fileName);
		ListIterator<Block> iterator = blockchain.listIterator();
		for(int i = 0; i < blockchain.size(); i++){
			Block block = iterator.next();
			x.println(block.getIndex());
			x.println(block.getTimestamp());
			x.println(block.getTransaction().getSender());
			x.println(block.getTransaction().getReceiver());
			x.println(block.getTransaction().getAmount());
			x.println(block.getNonce());
			x.println(block.getHash());
		}
		x.close();
	}

	public static void main (String[] args) throws IOException{

		Scanner scanner = new Scanner(System.in);

		System.out.println("Miner: ");
		String miner = scanner.nextLine();

		System.out.println("Read File: ");
		String file = scanner.nextLine();

		BlockChain blocks;
		blocks = BlockChain.fromFile(file);

		if(!blocks.validateBlockchain()){
			throw new IOException("Invalid BlockChain");
		}

		System.out.println("Make a transaction? yes/no");
		String yes = scanner.nextLine();

		boolean bool = true;

		if(yes.equals("no")){
			bool = false;
		}
	
		while(bool){

			System.out.println("Receiver: ");
			String receive = scanner.nextLine();

			System.out.println("Sender: ");
			String send = scanner.nextLine();

			System.out.println("Amount: ");
			String amnt = scanner.nextLine();

			if(Integer.parseInt(amnt) <= blocks.getBalance(send)){
				Transaction trans1 = new Transaction(send, receive, amnt);
				Block block1 = new Block(blocks.getBlockChain().get(blocks.getBlockChain().size()-1), trans1);
				blocks.addBlock(block1);
				System.out.println("Duration: " + block1.getDuration() + " Counter: " + block1.getCounter());
			}
			else{
				System.out.println("Insufficient Funds");
			}

			System.out.println("Make another transaction? yes/no");
			yes = scanner.nextLine();

			if(yes.equals("no")){
				bool = false;
			}

		}
		
		file = file.substring(0,file.length()-4);
		blocks.toFile(file + "_" + miner + ".txt");
		System.out.println("Created");

	}

	
}