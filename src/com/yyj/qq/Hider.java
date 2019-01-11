package com.yyj.qq;

import com.yyj.hide.BitmapExecute;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Hider extends JFrame implements ActionListener{

	private JPanel contentPane;
	public static JFileChooser chooser;
	FileFilter filter;
	private final JLabel lb1;
	private final JLabel lb2;
	private final JLabel lb3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hider frame = new Hider();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Hider() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 601, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("����BMP�ļ���Ϣ����");
		JButton btn1 = new JButton("\u9690\u85CF");
		btn1.setBounds(98, 264, 93, 23);
		contentPane.add(btn1);
		btn1.addActionListener(this);
		JButton btn2 = new JButton("\u63D0\u53D6");
		btn2.setBounds(388, 264, 93, 23);
		contentPane.add(btn2);

		JLabel label = new JLabel("\u9700\u9690\u85CF\u7684\u6587\u4EF6");
		label.setBounds(10, 71, 78, 15);
		contentPane.add(label);

		lb2 = new JLabel("");
		lb2.setBounds(49, 193, 199, 15);
		contentPane.add(lb2);

		JButton button1 = new JButton("���");
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file = browse();
				lb1.setText(file);
			}
		});
		button1.setBounds(98, 67, 93, 23);
		contentPane.add(button1);

		JButton button2 = new JButton("���");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file = browse();
				lb2.setText(file);
			}
		});
		button2.setBounds(98, 160, 93, 23);
		contentPane.add(button2);

		JLabel label_1 = new JLabel("\u9700\u9690\u85CF\u7684\u6587\u4EF6");
		label_1.setBounds(300, 164, 78, 15);
		contentPane.add(label_1);

		JButton button3 = new JButton("���");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String file = browse();
				lb3.setText(file);
			}
		});
		button3.setBounds(388, 160, 93, 23);
		contentPane.add(button3);

		JLabel lblbmp_1 = new JLabel("\u8F7D\u4F53BMP\u6587\u4EF6");
		lblbmp_1.setBounds(13, 164, 83, 15);
		contentPane.add(lblbmp_1);

		lb1 = new JLabel("");
		lb1.setBounds(49, 100, 199, 15);
		contentPane.add(lb1);

		lb3 = new JLabel("");
		lb3.setBounds(341, 193, 199, 15);
		contentPane.add(lb3);

		JLabel lblNewLabel = new JLabel("\u57FA\u4E8EBMP\u6587\u4EF6\u7684\u4FE1\u606F\u9690\u85CF");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("����", Font.BOLD, 16));
		lblNewLabel.setBounds(183, 26, 210, 23);
		contentPane.add(lblNewLabel);
		btn2.addActionListener(this);
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//����ѡ��ģʽ���ȿ���ѡ���ļ��ֿ���ѡ���ļ���
		String extj[] = { "jpeg","jpg" };
		filter = new FileNameExtensionFilter( "JPEG Image",extj);
		chooser.setFileFilter(filter);//�����ļ���׺������
		String extt[] = { "bmp","bmp" };
		filter = new FileNameExtensionFilter( "BMP Image",extt);
		chooser.setFileFilter(filter);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int retval;
		String selection = e.getActionCommand();
		if(selection.equals("����")) {
			File file = new File("2.bmp");
			chooser.setSelectedFile(file);//����Ĭ���ļ���
			retval = chooser.showSaveDialog(this);//��ʾ�������ļ����Ի���
			if(retval == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				try {
					BitmapExecute.DataSourceToBMP(lb1.getText(), lb2.getText(), file.getAbsolutePath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} else if(selection.equals("��ȡ")) {
			File file = new File("1.txt");
			chooser.setSelectedFile(file);//����Ĭ���ļ���
			retval = chooser.showSaveDialog(this);//��ʾ�������ļ����Ի���
			if(retval == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				try {
					BitmapExecute.BMPToDataSource(lb3.getText(), file.getAbsolutePath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}

	public String browse(){
		File file = null;
		int retval = chooser.showOpenDialog(this);
		if(retval == JFileChooser.APPROVE_OPTION) {//���ɹ���
			file = chooser.getSelectedFile();//�õ�ѡ����ļ���
		}
		return file.getAbsolutePath();
	}
}
