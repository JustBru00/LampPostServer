package com.gmail.justbru00.lamppostserver;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;

public class LampPostServerMain {
	
	public static boolean RUNNING = true;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
	            try {	            	
	                Thread.sleep(200);
	                System.out.println("\nReceived shutdown request from system. (CTRL-C)");
	                
	                NetworkServerManager.closeServer();
	                RUNNING = false;		                
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LampPostServerMain window = new LampPostServerMain();
					window.frame.setVisible(true);
					new Thread(() -> {						
							NetworkServerManager.startServer();							
						
					}).start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LampPostServerMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		JButton btnNewButton = new JButton("Lamp 1 ON");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NetworkServerManager.setLampState("LAMP1", true);
			}
		});
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("Lamp 1 OFF");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetworkServerManager.setLampState("LAMP1", false);
			}
		});
		panel_1.add(btnNewButton_3);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));
		
		JButton btnNewButton_1 = new JButton("Lamp 2 ON");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetworkServerManager.setLampState("LAMP2", true);
			}
		});
		panel_2.add(btnNewButton_1);
		
		JButton btnNewButton_4 = new JButton("Lamp 2 OFF");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetworkServerManager.setLampState("LAMP2", false);
			}
		});
		panel_2.add(btnNewButton_4);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JButton btnNewButton_2 = new JButton("Both ON");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetworkServerManager.setLampState("LAMP1", true);
				NetworkServerManager.setLampState("LAMP2", true);
			}
		});
		panel_3.add(btnNewButton_2);
		
		JButton btnNewButton_5 = new JButton("Both OFF");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetworkServerManager.setLampState("LAMP1", false);
				NetworkServerManager.setLampState("LAMP2", false);
			}
		});
		panel_3.add(btnNewButton_5);
		
	}

}
