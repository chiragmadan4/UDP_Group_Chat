package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GroupChatMember {
	

	public static void main(String[] args) {
		
		DatagramSocket datagramSocket;
		Scanner s = new Scanner(System.in);
		try {
			datagramSocket = new DatagramSocket();
			RecieveService rs = new RecieveService(datagramSocket);
			rs.start();
			while(true) {
				System.out.println("Enter message..");
				String msg = s.nextLine();
				/* enter "join" to join group chat
				 * enter "leave" to leave group chat
				 * any other msg will be considered as a msg to be sent and will be broadcasted.
				 */
				
				byte data[] = new String(msg).getBytes();
				datagramSocket.send(new DatagramPacket(data,data.length,InetAddress.getLocalHost(),8989));
		
			}
				
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}

class RecieveService extends Thread{
	DatagramSocket datagramSocket;
	public RecieveService(DatagramSocket d) {
		this.datagramSocket = d;
	}
	public void run() {
		while(true) {
			DatagramPacket responsePacket = new DatagramPacket(new byte[1000],1000);
			try {
				datagramSocket.receive(responsePacket);
				byte response[] = responsePacket.getData();
				String res = new String(response);
				if(res.equals("You have left."))
				System.out.println(res);
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
