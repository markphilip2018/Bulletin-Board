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
		System.out.println(serverIp);
		int port = Integer.valueOf(args[1]);
		System.out.println("port = " +port);
		int numberOfAccesses = Integer.valueOf(args[2]);
		int clientNumber = Integer.valueOf(args[3]);

		for (int i = 0; i < numberOfAccesses; i++) {
			System.out.println("clinet main started..");
			Socket socket = new Socket(serverIp, port);
			System.out.println("connected to server successfully.");

			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

			String msg = "writer "+clientNumber;
			int msgLength = msg.getBytes().length; // calculate the msg length in bytes

			dataOutputStream.writeInt(msgLength); // set the msg first to the length of the msg

			dataOutputStream.write(msg.getBytes()); // write the msg in Bytes

			dataOutputStream.flush(); // send the msg

			System.out.println("message 1 is sent.");
			
			DataInputStream dataInputStream;
			dataInputStream = new DataInputStream(socket.getInputStream());
			int length;
			byte[] dataBytes;
			String response;
			length = dataInputStream.readInt(); // i send the length at first

			dataBytes = new byte[length]; // create array as a buffer

			dataInputStream.read(dataBytes); // read the data into the buffer array

			response = new String(dataBytes); // convert to string
			
			clientReader.add(response);
			
			System.out.println(response);
 

			Long start = System.currentTimeMillis();

			while (System.currentTimeMillis() - start < (Math.random()*3000)) {
			}
		}
		String filename = "arg"+clientNumber+".txt";
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		for (int i = 0; i < clientReader.size(); i++) {
			writer.println(clientReader.get(i));
		}
		writer.close();
	}

}