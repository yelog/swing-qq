package com.yyj.qq;

import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.yyj.util.Translate;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class P2PClient extends JFrame implements ActionListener{

	public static TextField  jta1=null;
	public static JTextArea  jta=null;
	public static JButton    jbt=null;
	public static JScrollPane jsp=null;
	public static JPanel     jpl=null;
	public static JButton sendFile = null;
	public static String name;
	public static JFileChooser chooser;
	FileFilter filter;
	FileInputStream fis = null;
	byte[] sendBytes = null;
	byte[] sendBytes2 = null;
	int length = 0;
	public P2PClient()
	{
		//创建各组件
		jta=new JTextArea();
		jsp=new JScrollPane(jta);
		jta1=new TextField(15);
		jbt=new JButton("发送");
		jbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = P2PClient.jta1.getText().trim();
				P2PClient.jta1.setText("");
				P2PClient.jta.append(Login.name+"："+str+"\r\n");
				clientjava.mess.clear();
				clientjava.mess.put(P2PClient.name, str);
				str = "私聊消息"+Translate.transMapToString(clientjava.mess);
				try {
					clientjava.dos.writeUTF(str);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		jpl=new JPanel();

		sendFile= new JButton("文件");
		sendFile.addActionListener(this);

		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//设置选择模式，既可以选择文件又可以选择文件夹
		String extj[] = { "jpeg","jpg" };
		filter = new FileNameExtensionFilter( "JPEG Image",extj);
		chooser.setFileFilter(filter);//设置文件后缀过滤器
		String extt[] = { "bmp","bmp" };
		filter = new FileNameExtensionFilter( "BMP Image",extt);
		chooser.setFileFilter(filter);

		setVisible(true);

		jpl.add(sendFile);
		//显示布局
		jpl.add(jta1);
		jpl.add(jbt);

		getContentPane().add(jsp,"Center");
		getContentPane().add(jpl,"South");

		//创建主窗口
		this.setBounds(200, 200, 300, 300);
		this.setTitle("群聊");
		this.setVisible(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			clientjava.dos.writeUTF("传输文件"+P2PClient.name);
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		int retval = chooser.showOpenDialog(this);//显示"保存文件"对话框
		if(retval == JFileChooser.APPROVE_OPTION) {//若成功打开
			File file = chooser.getSelectedFile();//得到选择的文件名
			System.out.println("File to open " + file);
			try {
				try {
					fis = new FileInputStream(file);
					sendBytes = new byte[1024];
					while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
						clientjava.dos.write(sendBytes, 0, length);
						clientjava.dos.flush();
					}
				}catch(Exception e2){
					e2.printStackTrace();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

}
