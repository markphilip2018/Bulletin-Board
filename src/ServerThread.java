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

			dataInputStream = new DataInputStream(socket.getInputStream());

			length = dataInputStream.readInt();

			dataBytes = new byte[length];

			dataInputStream.read(dataBytes);

			msg = new String(dataBytes);

			String[] splittedArray = msg.split(" ");

			if (splittedArray[0].equals("reader"))
				MainServer.increaseRnum();

			Thread.sleep((long) (Math.random() * 1000));

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

				if (arr[2] == arr[0])
					MainServer.closeServer();

			} else if (splittedArray[0].equals("writer")) {

				// read the value
				MainServer.changeValue(id);
				String msg = rseq + " " + arr[0];

				int msgLength = msg.getBytes().length;

				dataOutputStream.writeInt(msgLength);

				dataOutputStream.write(msg.getBytes());

				dataOutputStream.flush();

				if (arr[2] == arr[0])
					MainServer.closeServer();

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
