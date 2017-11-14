import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	String hostName = "localhost";
	int portNumber = 2226;

	
	Socket socket;
	DataOutputStream dos;
	
	PrintWriter outputStream;
	
	Client(int numberOfFiles){		
	}
	
	
	public void send(String xmlFile){
		
		try {
			//dos.writeUTF("OK");
			//Get XML file
			socket = new Socket(hostName, portNumber);
			
			
			outputStream = new PrintWriter(new DataOutputStream(
					socket.getOutputStream()));
			
			
			
			File fileToSend = new File(xmlFile);
			//Open fileInputStream to object file and see if file exists
			FileInputStream fileInputStream = new FileInputStream(fileToSend);			
			
			
			long contentLength = fileToSend.length();
			
			byte[] buffer = new byte[(int)contentLength];
			
			
			outputStream.print("Content-length:" + contentLength + "\r\n");
			outputStream.print("\r\n");
			outputStream.flush();
			
			
			fileInputStream.read(buffer);
			
			socket.getOutputStream().write(buffer);
			socket.getOutputStream().flush();
			

			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		

		
	}
	
	
	public void signalFinished(){
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("DONE");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
}
