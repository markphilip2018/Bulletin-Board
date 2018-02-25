import java.io.IOException;
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
	static int rnum;

	public static void main(String[] args) throws IOException {
		int rseq = 0;
		System.out.println("server main started...");

		// try to start the server
		ServerSocket serverSocket = new ServerSocket(8000);
		System.out.println("server started successfully.");

		// int clientNumber = -1;

		while (true) {
			rseq++;
			System.out.println("waiting for client...");
			Socket clientSocket = serverSocket.accept();

			new ServerThread(clientSocket, rseq).start();
			System.out.println("new client connected.");
		}

	}

	public static int[] serviceReader(int id) {
		
		int[] arr = new int[2];
		try {
			semaphore.acquire();
			arr[0] = ++sSeq;
			arr[1] = value;
			String line = sSeq + " " + value + " " + id +" "+rnum;
			System.out.println(line);
			readerTuples.add(line);
			
			
			semaphore.release();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return arr;

	}

	public static int getValue() {
		

		// semaphore.acquire();
		int num = value;
		semaphore.release();
		// } catch (InterruptedException e) {

		// e.printStackTrace();
		// }

		return num;

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

	/*public static void writeReaderTuple(int sSeqIn, int valueIn, int id) {
		try {
			semaphorePrint.acquire();
			String line = sSeqIn + " " + valueIn + " " + id+" "+rnum;
			readerTuples.add(line);
			semaphorePrint.release();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		System.out.println(MainServer.readerTuples.getLast());
	}*/

}
