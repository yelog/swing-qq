package com.yyj.qq;

import java.awt.BorderLayout;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientFrame extends JFrame {

	private JPanel contentPane;
	public static JButton getUsers;
	public static DefaultMutableTreeNode root;
	/**
	 * Create the frame.
	 */
	public ClientFrame(Map<String,Socket> sockets) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 244, 433);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		init(sockets);
		this.setTitle(Login.name);

		this.setVisible(false);

	}

	public void init(Map<String,Socket> sockets){
		root= new DefaultMutableTreeNode("好友列表");
		DefaultMutableTreeNode node = new DefaultMutableTreeNode("群聊");
		root.add(node);
		if(sockets!=null){
			Set<Map.Entry<String, Socket>> set = sockets.entrySet();
			for (Iterator<Map.Entry<String, Socket>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, Socket> entry = (Map.Entry<String, Socket>) it.next();
				DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(entry.getKey());
				root.add(node1);
			}
		}

		final JTree tree = new JTree(root);
		tree.expandRow(0);
		getContentPane().add(tree);

		tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
				String name=node.toString();

				if(node.isLeaf()){
					if(name.equals("群聊")){
						ClientStart.client.setVisible(true);
						ClientStart.client.setTitle(name);
					}else{
						P2PClient p2pClient = null;
						int i =0;
						Set<Map.Entry<String, JFrame>> set = ClientStart.jframes.entrySet();
						for (Iterator<Map.Entry<String, JFrame>> it = set.iterator(); it.hasNext();) {
							Map.Entry<String, JFrame> entry = (Map.Entry<String, JFrame>) it.next();
							MyClient.jta.append(entry.getKey()+"："+entry.getValue()+"\r\n");
							if(entry.getKey().equals(name)){
								p2pClient = (P2PClient) entry.getValue();
								i++;
							}
						}
						if(i==0){
							p2pClient = new P2PClient();
						}
						p2pClient.name=name;
						p2pClient.setTitle(name);
						p2pClient.setVisible(true);
						ClientStart.jframes.put(name, p2pClient);
					}
				}
			}
		});
		getUsers= new JButton("刷新");
		getUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientStart.init();
			}
		});
		contentPane.add(getUsers, BorderLayout.SOUTH);

	}

}
