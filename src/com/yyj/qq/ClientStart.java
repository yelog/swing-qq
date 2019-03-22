package com.yyj.qq;

import com.yyj.util.LanIP;
import com.yyj.util.Translate;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientStart implements ActionListener{
	public static Socket s;
	static DataOutputStream dos;
	private DataInputStream dis;
	static MyClient client;
	private static ClientFrame mainframe;
	private static Map<String,Socket> sockets=null;
	static Map<String,JFrame> jframes=new HashMap<>();
	static Map <String,String> mess = new HashMap<>();
	private static P2PClient p2p;
	static Wait wait;

	public static void main(String[] args) {
		new Login();
		client = new MyClient();
		wait = new Wait();
		mainframe = new ClientFrame(sockets);
		p2p = new P2PClient();
	}
	public void connect(){
		MyClient.jbt.addActionListener(this);
		MyClient.jta1.addActionListener(this);
		P2PClient.jbt.addActionListener(this);

		LanIP ip = new LanIP();
//		ArrayList<String> list=ip.getLanIPArrayList();
//		System.out.println(Arrays.asList(list));
//		for(int i=0;i<list.size();i++){
//			try {
//				s=new Socket(list.get(i),9988);
//				System.out.println(list.get(i));
//				break;
//			} catch (IOException ignored) {
//
//			}
//		}
		try {
			s=new Socket("127.0.0.1",9988);
		} catch (IOException e) {
			e.printStackTrace();
		}
		wait.dispose();
		mainframe.setVisible(true);
		try {
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			new Thread(new SendThread()).start();

//			InputStreamReader isr=new InputStreamReader(s.getInputStream());
//			BufferedReader br=new BufferedReader(isr);
//
//			pw=new PrintWriter(s.getOutputStream(),true);
//			while(true)
//			{
//				//不停在服务器读取消息
//				String info=br.readLine();
//				MyClient.jta.append("客户端对服务器说："+info+"\r\n");
//
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==MyClient.jbt)
		{
			String info="开始群聊"+MyClient.jta1.getText();

			try {
				dos.writeUTF(info);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			MyClient.jta1.setText("");
		}

		if(e.getSource()==MyClient.jta1){
			String info="开始群聊"+MyClient.jta1.getText();

			try {
				dos.writeUTF(info);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			MyClient.jta1.setText("");
		}

		if(e.getSource()==P2PClient.jbt){
			String str = MyClient.jta1.getText().trim();
			P2PClient.jta1.setText("");
			P2PClient.jta.append(Login.name+"："+str+"\r\n");
			mess.clear();
			mess.put(P2PClient.name, str);
			str = "私聊消息"+Translate.transMapToString(mess);
			try {
				dos.writeUTF(str);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		if(e.getSource()==ClientFrame.getUsers){
			init();

		}

	}

	public static void init(){
		try {

			dos.writeUTF("获取好友");

//				//不停在服务器读取消息
//				String info=dis.readUTF();
//				System.out.println(info);
//				Map<String,Socket> sockets = Translate.transStringToMap(info);
//				MyClient.jta.append("======================\r\n=====好友列表==========\r\n");
//
//		        Set<Map.Entry<String, Socket>> set = sockets.entrySet();
//		        for (Iterator<Map.Entry<String, Socket>> it = set.iterator(); it.hasNext();) {
//		            Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) it.next();
//		            MyClient.jta.append(entry.getKey() + "--->" + entry.getValue()+"\r\n");
//		        }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			MyClient.jta.append("连接服务器失败\r\n");
		}
	}

	/**
	 * 客户端接收消息的线程呦...
	 *
	 */
	class SendThread implements Runnable{
		private String str;
		private boolean iConnect = false;

		public void run(){
			try {
				mainframe.setTitle("当前用户："+Login.name);
				dos.writeUTF(Login.name);
				dos.writeUTF("获取好友");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iConnect = true;
			recMsg();

		}

		/**
		 * 消息，看招，哪里跑...（客户端接收消息的实现）
		 * @throws IOException
		 */
		public void recMsg() {
			try {
				while(iConnect){
					str = dis.readUTF();
					if(str.length()>=4&&!str.equals("传输文件")){
						if(str.subSequence(0, 4).equals("获取好友")){
							//创建好友的叶子
							DefaultMutableTreeNode node;
							String info = str.substring(4);
							//String 转换为 Map
							sockets = Translate.transStringToMap(info);
							mainframe.setVisible(false);
							mainframe = new ClientFrame(sockets);
							mainframe.setVisible(true);

						}else if(str.subSequence(0, 4).equals("私聊消息")){
							String message = str.substring(4);
							mess = Translate.transStringToMap(message);
							int i = 0;
							Set<Map.Entry<String, String>> set = mess.entrySet();
							for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
								Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
								//遍历本地的jframe的map，获取或创建frame；
								Set<Map.Entry<String, JFrame>> set1 = jframes.entrySet();
								for (Iterator<Map.Entry<String, JFrame>> it1 = set1.iterator(); it1.hasNext();) {
									Map.Entry<String, JFrame> entry1 = (Map.Entry<String, JFrame>) it1.next();
									if(entry.getKey().equals(entry1.getKey())){
										System.out.println("I");
										p2p = (P2PClient) entry1.getValue();
										p2p.jta.append(entry.getKey()+":"+entry.getValue()+"\r\n");
										p2p.setVisible(true);
										i++;
										break;
									}
								}
								if(i==0){
									P2PClient p2pClient = new P2PClient();
									p2pClient.name=entry.getKey();
									p2pClient.setTitle(entry.getKey());
									p2pClient.setVisible(true);
									p2pClient.jta.append(entry.getKey()+":"+entry.getValue()+"\r\n");
									ClientStart.jframes.put(entry.getKey(), p2pClient);

								}

							}



						}else if(str.substring(0,4).equals("开始群聊")){

							String message = str.substring(4);
							mess = Translate.transStringToMap(message);
							client.setVisible(true);
							Set<Map.Entry<String, String>> set = mess.entrySet();
							for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
								Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
								MyClient.jta.append(entry.getKey()+"："+entry.getValue()+"\r\n");

							}
						}else
							MyClient.jta.append("消息提醒："+str+"\r\n");
					}else{
						System.out.println("第三部");
						receiveFile(s);
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public void receiveFile(Socket socket) {
		byte[] inputByte = null;
		int length = 0;
		DataInputStream dis = null;
		FileOutputStream fos = null;
		try {
			try {
				dis = new DataInputStream(socket.getInputStream());
				inputByte = new byte[1024];
				System.out.println("开始接收数据...");
				File file = new File("3.jpg");
				P2PClient.chooser.setSelectedFile(file);//设置默认文件名
				int retval = P2PClient.chooser.showSaveDialog(p2p);//显示“保存文件”对话框
				if(retval == JFileChooser.APPROVE_OPTION) {
					file = P2PClient.chooser.getSelectedFile();
					System.out.println("File to save " + file);
				}
				fos = new FileOutputStream(file);


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