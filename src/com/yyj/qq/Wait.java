package com.yyj.qq;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

public class Wait extends JFrame {

	public static void main(String[] args) {
		new Wait().setVisible(true);;

	}

	private JPanel contentPane;
	public static JButton getUsers;
	public static DefaultMutableTreeNode root;
	private JTable table;
	/**
	 * Create the frame.
	 */
	public Wait() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 244, 433);
		this.setBackground(Color.black);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.getContentPane().setBackground(Color.BLACK);
		table = new JTable();
		contentPane.add(table, BorderLayout.NORTH);
		JPanel imagePanel = (JPanel) getContentPane();
		imagePanel.setOpaque(false);
		ImageIcon img = new ImageIcon("3.gif");
		JLabel label = new JLabel(img);
		getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		label.setBounds(5,0,230, 274);
		Container cp=getContentPane();
		cp.setLayout(null);
		((JPanel)cp).setOpaque(false);



		this.setTitle(Login.name);

		this.setVisible(false);

	}

}
