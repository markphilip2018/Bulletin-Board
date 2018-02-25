import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket;
	private DataInputStream dataInputStream;
	private int length;
	private byte[] dataBytes;
	private String msg;
	private int rseq;

	public ServerThread(Socket socket, int rseq) {
		this.socket = socket;
		this.rseq = rseq;

	}

	@Override
	public void run() {
		try {

			System.out.println("wait for the msg.");
			dataInputStream = new DataInputStream(socket.getInputStream());

			length = dataInputStream.readInt(); // i send the length at first

			dataBytes = new byte[length]; // create array as a buffer

			dataInputStream.read(dataBytes); // read the data into the buffer array

			msg = new String(dataBytes); // convert to string
			System.out.println("client " + " :" + msg);

			MainServer.increaseRnum();
			
			Thread.sleep((long)(Math.random() * 1000));

			String[] splittedArray = msg.split(" ");

			int id = Integer.valueOf(splittedArray[1]);
			
			int[] arr = MainServer.serviceReader(id);

			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

			if (splittedArray[0].equals("reader")) {

				// read the value
				String msg = rseq + " " + arr[0] + " " + arr[1];

				int msgLength = msg.getBytes().length;

				dataOutputStream.writeInt(msgLength);

				dataOutputStream.write(msg.getBytes());

				dataOutputStream.flush();
				
				MainServer.decreaseRnum();
			} else if (splittedArray[0].equals("writer")) {

				// read the value
				MainServer.changeValue(id);
				String msg = rseq + " " + arr[0] ;

				int msgLength = msg.getBytes().length;

				dataOutputStream.writeInt(msgLength);

				dataOutputStream.write(msg.getBytes());

				dataOutputStream.flush();
				
				MainServer.decreaseRnum();

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
