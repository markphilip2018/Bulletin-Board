import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class MainServer {

	static int sSeq = 0;
	static int value = -1;
	static Semaphore semaphore = new Semaphore(1);
	static Semaphore semaphoreRnum = new Semaphore(1);
	static Semaphore semaphoreValue = new Semaphore(1);
	static LinkedList<String> readerTuples = new LinkedList<String>();
	static LinkedList<String> writerTuples = new LinkedList<String>();
	static int rnum = 0;
	static int readersSize;
	static int writersSize;
	static int numberOfAccess;
	static int totalConnection;

	public static void main(String[] args) throws IOException {
		int rseq = 0;
		System.out.println("server main started...");

		// try to start the server
		ServerSocket serverSocket = new ServerSocket(8000);
		System.out.println("server started successfully.");

		// int clientNumber = -1;

		readersSize = Integer.valueOf(args[0]);
		writersSize = Integer.valueOf(args[1]);
		numberOfAccess = Integer.valueOf(args[2]);

		totalConnection = (readersSize + writersSize) * numberOfAccess;

		while (true) {
			rseq++;
			System.out.println("waiting for client...");
			Socket clientSocket = serverSocket.accept();

			new ServerThread(clientSocket, rseq).start();
			System.out.println("new client connected.");
		}

	}

	public static int[] serviceReader(int id) {

		int[] arr = new int[3];
		try {
			semaphore.acquire();
			arr[0] = ++sSeq;
			arr[1] = value;
			arr[2] = totalConnection;
			
			if (id < readersSize) {
				String line = sSeq + " " + value + " " + id + " " + rnum;
				System.out.println(line);
				readerTuples.add(line);
			} else {
				String line = sSeq + " " + id + " " + id ;
				System.out.println(line);
				writerTuples.add(line);
			}
			semaphore.release();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return arr;

	}

	public static void increaseRnum() {

		try {
			semaphoreRnum.acquire();
			rnum++;
			semaphoreRnum.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void decreaseRnum() {

		try {
			semaphoreRnum.acquire();
			rnum--;
			semaphoreRnum.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void changeValue(int value) {
		try {
			semaphoreValue.acquire();
			MainServer.value = value;
			semaphoreValue.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void closeServer() {

		PrintWriter writer;
		try {
			writer = new PrintWriter("serverlog.txt", "UTF-8");
			writer.println("Readers :");
			writer.println("sSeq oVal rID rNum");
			for (int i = 0; i < readerTuples.size(); i++) {
				writer.println(readerTuples.get(i));
			}
			writer.println("writers :");
			writer.println("sSeq oVal wID");
			for (int i = 0; i < writerTuples.size(); i++) {
				writer.println(writerTuples.get(i));
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
		
	}

}
