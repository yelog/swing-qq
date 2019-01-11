package com.yyj.qq;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class MyClient extends JFrame{

	public static TextField  jta1=null;
	public static JTextArea  jta=null;
	public static JButton    jbt=null;
	public static JScrollPane jsp=null;
	public static JPanel     jpl=null;
	public static JButton viewuser = null;

	public MyClient()
	{
		//创建各组件
		jta=new JTextArea();
		jsp=new JScrollPane(jta);
		jta1=new TextField(15);
		jbt=new JButton("发送");

		jpl=new JPanel();

		viewuser= new JButton("好友");
		jpl.add(viewuser);
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

}