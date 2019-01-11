package com.yyj.qq;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
	public static String name;
	private JPanel contentPane;
	public static TextField username;
	public static JLabel label;
	public static JButton loginbtn;
	public static JLabel warning;

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("厦门理工学院网络工程专用聊天工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		username = new TextField();
		username.setBounds(104, 87, 219, 24);
		contentPane.add(username);

		JPanel imagePanel = (JPanel) getContentPane();
		imagePanel.setOpaque(false);
		ImageIcon img = new ImageIcon("1.jpg");
		JLabel label = new JLabel(img);
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		label.setBounds(0,0,444, 274);
		Container cp=getContentPane();
		cp.setLayout(null);
		((JPanel)cp).setOpaque(false);



		label = new JLabel("用户名：");
		label.setBounds(26, 91, 54, 15);
		contentPane.add(label);

		loginbtn = new JButton("登陆");
		init();
		loginbtn.setBounds(164, 146, 93, 41);
		contentPane.add(loginbtn);

		warning = new JLabel("");
		warning.setForeground(Color.RED);
		warning.setBounds(164, 51, 93, 15);
		contentPane.add(warning);
		this.username.addActionListener(new TFListener());
		this.setVisible(true);
	}
	class TFListener implements ActionListener{
		private String str;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(username.getText().equals("")){
				warning.setText("请输入用户名");
			}else{
				dispose();
				name=username.getText();
				new thing1().start();
				new thing2().start();
			}
		}

	}


	public void init(){
		loginbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(username.getText()==""){
					warning.setText("请输入用户名");
				}else{
					dispose();
					name=username.getText();
					new thing1().start();
					new thing2().start();
				}
			}
		});
	}
}

class thing1 extends Thread{
	@Override
	public void run() {
		clientjava.wait.setVisible(true);
	}
}

class thing2 extends Thread{
	@Override
	public void run() {
		new clientjava().connect();
	}
}


