package com.apollon.util;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * @author Matteo - matteo.manili@gmail.com
 *
 */
public class SnakeMain extends JFrame {

	public SnakeMain() {
		add(new Snake());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 500);
		setResizable(false);
		setTitle("Snake Game");
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SnakeMain();
			}

		});

	}
}
