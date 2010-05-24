package com.navy.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	/**
	 * @param args
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		ServerSocket server;
		try {
			server = new ServerSocket(5678);

System.out.println("started");
			Socket client = server.accept();
System.out.println("connected");
			ObjectInputStream in = new ObjectInputStream(client
					.getInputStream());

			ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

//			while (true) {

//				String str = in.readLine();

//				System.out.println(str);

//				out.println("has receive....");
//
//				out.flush();
//
//				if (str.equals("end"))
//
//					break;
//
//			}
			System.out.println("starting read");
			System.out.println(in.readObject());
			Message msg = new Message();
			msg.setCommand("server");
			msg.setDescription("server description");
			out.writeObject(msg);
			//out.println(str);

			out.flush();




			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
