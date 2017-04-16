package com.mapeditor.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NamingWindows extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7809848140563168852L;

	private JPanel jPanel1;

	JButton b_open = new JButton("Okay");

	public String fileName;

	JTextField textBox = new JTextField("", 20);

	public NamingWindows() {

		this.setResizable(false);
		this.setTitle("Name");
		this.setSize(240, 320);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		jPanel1 = new JPanel();

		jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

		b_open.addActionListener(this);

		jPanel1.add(textBox);
		jPanel1.add(b_open, -1);

		this.add(jPanel1);

		this.pack();

		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (textBox.getText().length() > 0)
			fileName = textBox.getText();

	}
}