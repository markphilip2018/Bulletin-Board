import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class ClientWriter {

	public static void main(String[] args) throws UnknownHostException, IOException {

		LinkedList<String> clientReader = new LinkedList<>();

		String serverIp = args[0];
		int port = Integer.valueOf(args[1]);
		int numberOfAccesses = Integer.valueOf(args[2]);
		int clientNumber = Integer.valueOf(args[3]);

		for (int i = 0; i < numberOfAccesses; i++) {

			Socket socket = new Socket(serverIp, port);
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

			String msg = "writer " + clientNumber;
			int msgLength = msg.getBytes().length;
			dataOutputStream.writeInt(msgLength);
			dataOutputStream.write(msg.getBytes());
			dataOutputStream.flush();

			DataInputStream dataInputStream;
			dataInputStream = new DataInputStream(socket.getInputStream());
			int length;
			byte[] dataBytes;
			String response;
			length = dataInputStream.readInt();
			dataBytes = new byte[length];
			dataInputStream.read(dataBytes);
			response = new String(dataBytes);
			clientReader.add(response);

			Long start = System.currentTimeMillis();

			while (System.currentTimeMillis() - start < (Math.random() * 3000)) {
			}
		}
		String filename = "log" + clientNumber + ".txt";
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		for (int i = 0; i < clientReader.size(); i++) {
			writer.println(clientReader.get(i));
		}
		writer.close();
	}

}