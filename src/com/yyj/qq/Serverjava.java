package com.yyj.qq;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;

import com.yyj.util.Translate;
public class Serverjava implements ActionListener{

	List<ClientThread> clients = new ArrayList<ClientThread>();
	public static Map<String,Socket> sockets = new HashMap<String, Socket>();
	Map<String,String> mess = new HashMap<String,String>();
	//把信息发送给客户端的对象
	PrintWriter pw=null;
	ServerSocket ss=null;
	public static void main(String[] args) {

		new MyServer();
		new Serverjava().start();
	}

	public void start(){
		MyServer.jbt.addActionListener(this);
		MyServer.viewuser.addActionListener(this);
		try {
			boolean iConnect = false;
			//服务器端监听9988端口
			ss=new ServerSocket(9988);
			iConnect = true;
			MyServer.jta.append("服务器打开成功===等待用户接入\r\n");

			while(iConnect){
				Socket s = ss.accept();
				ClientThread currentClient = new ClientThread(s);//创建线程引用
				clients.add(currentClient);//把当前客户端加入集合
				new Thread(currentClient).start();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 给每个客户端留个家（客户端的进程）
	 *
	 */
	class ClientThread implements Runnable {
		/*
		 * 成员变量又来啦...
		 */
		private String name;
		private Socket s;
		private DataInputStream dis;
		private DataOutputStream dos;
		private String str;
		private boolean iConnect = false;
		private FileOutputStream fos = null;
		byte[] inputByte = null;
		int length;

		/**
		 * 小构一下
		 */
		ClientThread(Socket s){
			this.s = s;
			iConnect = true;
		}

		public void run(){
			System.out.println("run方法启动了！");
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				name = dis.readUTF();
				MyServer.jta.append("用户上线："+name+"\r\n");
				Serverjava.sockets.put(name, s);

				while(iConnect){
					System.out.println("RUN方法中的while循环启动，正在等待客户端的发送消息...");
					dis = new DataInputStream(s.getInputStream());
					str = dis.readUTF();
					if(str.length()>=4&&!str.substring(0, 4).equals("传输文件")){
						if(str.subSequence(0, 4).equals("开始群聊")){
							MyServer.jta.append(name+"发起群聊\r\n");
							MyServer.jta.append(name+":"+str.substring(4)+"\r\n");
							mess.clear();
							mess.put(name, str.substring(4));
							String me="开始群聊"+Translate.transMapToString(mess);
							MyServer.jta.append(name+":"+me+"\r\n");
							for(int i=0; i<clients.size(); i++){
								System.out.println("转发消息中..."+i);
								ClientThread c = clients.get(i);
								c.sendMsg(me);
							}

						}else if(str.subSequence(0, 4).equals("私聊消息")){
							String des=null,con=null;
							MyServer.jta.append(name+"发起私聊\r\n");
							mess = Translate.transStringToMap(str.substring(4));
							Set<Map.Entry<String, String>> set = mess.entrySet();
							for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
								Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
								des=entry.getKey();
								con=entry.getValue();
							}
							mess.clear();
							mess.put(name, con);
							String me="私聊消息"+Translate.transMapToString(mess);
							MyServer.jta.append(name+":"+me+"\r\n");
							for(int i=0; i<clients.size(); i++){
								System.out.println("转发消息中..."+i);
								ClientThread c = clients.get(i);
								if(c.name.equals(des)){
									c.sendMsg(me);
								}
							}
						}
						else if(str.equals("获取好友")){
							MyServer.jta.append(name+":"+str+"\r\n");
							for(int i=0; i<clients.size(); i++){
								System.out.println("转发消息中..."+i);
								ClientThread c = clients.get(i);
								String users ="获取好友"+Translate.transMapToString(sockets);
								if(c.name==this.name){
									c.sendMsg(users);
								}
							}
						}else{
							MyServer.jta.append(name+":"+str+"\r\n");
							for(int i=0; i<clients.size(); i++){
								System.out.println("转发消息中..."+i);
								ClientThread c = clients.get(i);
								c.sendMsg(str);
							}
						}
					}else{
						System.out.println("I am here");
						String des = str.substring(4);
						System.out.println(des);
						receiveFile(s);
						for(int i = 0; i<clients.size(); i++){
							ClientThread c = clients.get(i);
							if(c.name.equals(des)){
								c.sendFile();
							}
						}

					}


				}
			} catch (IOException e) {
				e.printStackTrace();
				MyServer.jta.append("用户："+name+" 下线了\r\n");
				Serverjava.sockets.remove(name);
			}

		}

		/**
		 * 转发消息，我做主...
		 * 将送至服务器的消息发送给每个连接到的客户端
		 */
		public void sendMsg(String str){
			try {
				System.out.println("创建输出管道！");
				dos = new DataOutputStream(this.s.getOutputStream());
				System.out.println("正在向客户端写消息！");
				dos.writeUTF(str);
				System.out.println("正在向客户端写消息成功！"+str);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		public void sendFile(){
			FileInputStream fis = null;
			byte[] sendBytes = null;
			try {
				dos = new DataOutputStream(this.s.getOutputStream());
				dos.writeUTF("传输文件");
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			File file = new File("c:/2.jpg");
			System.out.println("File to open " + file);
			try {
				try {
					fis = new FileInputStream(file);
					sendBytes = new byte[1024];
					while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
						dos.write(sendBytes, 0, length);
						dos.flush();
					}
				}catch(Exception e2){
					e2.printStackTrace();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}



	}



	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//如果用户按下发送按钮
		if(arg0.getSource()==MyServer.jbt)
		{
			//把内容发送给客户端
			String info=MyServer.jta1.getText();

			MyServer.jta.append("服务器对客户端说："+info+"\r\n");

			pw.println(info);
			//清空发送内容
			MyServer.jta1.setText("");
		}

		if(arg0.getSource()==MyServer.viewuser){
			MyServer.jta.append("======================\r\n=====好友列表==========\r\n");

			Set<Map.Entry<String, Socket>> set = sockets.entrySet();
			for (Iterator<Map.Entry<String, Socket>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) it.next();
				MyServer.jta.append(entry.getKey() + "--->" + entry.getValue()+"\r\n");
			}
		}
	}
	/**
	 * 接受客户端的发来的文件
	 * @param socket
	 */
	public static void receiveFile(Socket socket) {
		byte[] inputByte = null;
		int length = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try {
			try {
				dis = new DataInputStream(socket.getInputStream());
				fos = new FileOutputStream(new File("c:/2.jpg"));
				inputByte = new byte[1024];
				System.out.println("开始接收数据...");
				while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
					System.out.println(length);
					fos.write(inputByte, 0, length);
					fos.flush();
					if(length!=1024){
						break;
					}
				}
				System.out.println("完成接收");
			} catch(Exception e1){
				e1.printStackTrace();
			}finally{
				fos.close();
			}
		} catch (Exception e) {
		}
	}


}