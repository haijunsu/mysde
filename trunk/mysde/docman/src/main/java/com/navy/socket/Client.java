package com.navy.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	static Socket server;

	public static void main(String[] args) throws Exception {

		server = new Socket(InetAddress.getLocalHost(), 5678);

		System.out.println(1);
		ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
		System.out.println(2);
//		out.useProtocolVersion(ObjectOutputStream.PROTOCOL_VERSION_1);
		System.out.println(3);
		ObjectInputStream in = new ObjectInputStream(server
				.getInputStream());
		System.out.println(4);

	//	BufferedReader wt = new BufferedReader(new InputStreamReader(System.in));

		//while (true) {

		//	String str = wt.readLine();

		System.out.println(5);
		Message msg = new Message();
			msg.setCommand("client");
			msg.setDescription("client description");
			out.writeObject(msg);
System.out.println(6);
			//out.println(str);

			out.flush();
System.out.println(7);


			System.out.println(in.readObject());

		//}

		server.close();

	}
}
