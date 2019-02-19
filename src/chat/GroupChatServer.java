package chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

class ChatMember{
	InetAddress address;
	int port;
	public ChatMember(InetAddress a,int p) {
		this.address = a;
		this.port = p;
	}
}


public class GroupChatServer {

	public static void main(String[] args) {
		
		ArrayList<ChatMember> members = new ArrayList<>();
		
		try {
			
			DatagramSocket datagramSocket = new DatagramSocket(8989);
			while(true) {
				
				DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
				System.out.println("Waiting for packets..");
				datagramSocket.receive(packet);
				
				InetAddress add = packet.getAddress();
				int port = packet.getPort();
				
				byte byteData[] = packet.getData();
				String data = new String(byteData);
				//Handling Join Request
				if(data.trim().equals("join")) {
					
					System.out.println("Join request received..");
					
					//add to members
					members.add(new ChatMember(add,port));
					
					//send response accepted
					String response = "Join request accepted";
					byte res[] = response.getBytes();
					datagramSocket.send(new DatagramPacket(res,res.length,add,port));
					
					System.out.println("New member joined.");
				}
				else if(data.trim().equals("leave")) {
					
					System.out.println("Leave request received..");
					
					for(ChatMember member :members) {
						if(member.port == port) {
							members.remove(member);
							System.out.println("Member removed");
						}
					}
					
					//send response removed
					String response = "You have left.";
					byte res[] = response.getBytes();
					datagramSocket.send(new DatagramPacket(res,res.length,add,port));
					
				}else {
					
					//broadcast message to all members except sender
					String msg = port+": "+ data;
					byte msg1[] = msg.getBytes();
					
					for(ChatMember member:members) {
						datagramSocket.send(new DatagramPacket(msg1, msg1.length, member.address,member.port));	
					}
					System.out.println("msg broadcasted..");
				}
			}
			
			
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
