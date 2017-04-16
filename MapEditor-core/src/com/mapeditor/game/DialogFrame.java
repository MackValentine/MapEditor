package com.mapeditor.game;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.badlogic.gdx.files.FileHandle;
import com.mapeditor.map.Event;
import com.mapeditor.screen.Screen;

import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class DialogFrame extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5096765149469727396L;

	private JPanel contentPane;
	private JTextField textField;

	private JButton btnChangeBack;

	private JButton btnChangeFace;

	private FileOppener fileOppener;

	private DialogPanel dialogPanel;

	private Event event;

	private JTextArea textArea;

	/**
	 * Create the frame.
	 */
	public DialogFrame(Event e) {
		event = e;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(Screen.Width + 144, Screen.Height + 75);
		// setBounds(0, 0, Screen.Width + 144, Screen.Height + 75);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		btnChangeFace = new JButton("Change Face");
		panel_1.add(btnChangeFace);

		btnChangeFace.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				fileOppener = new FileOppener("faces");
			}
		});

		btnChangeBack = new JButton("Change Back");
		panel_1.add(btnChangeBack);

		btnChangeBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileOppener = new FileOppener("cinematics_back");
			}
		});

		JButton btnSave = new JButton("Save");
		panel_1.add(btnSave);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.WEST);

		textArea = new JTextArea();
		textArea.setColumns(10);
		textArea.setText(e.command.dialog);
		scrollPane.setViewportView(textArea);

		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				repaint();
			}

			public void keyPressed(KeyEvent ke) {
				repaint();
			}

			public void keyReleased(KeyEvent ke) {
				repaint();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				event.command.back = dialogPanel.back_name;
				event.command.face = dialogPanel.face_name;
				event.command.dialog = textArea.getText();
			}
		});

		dialogPanel = new DialogPanel(textArea);
		contentPane.add(dialogPanel, BorderLayout.CENTER);
		String s;
		String s2;
		if (e.command.back != null) {
			s = MapEditor.s + "/cinematics/faces/" + e.command.face;
		} else {
			s = MapEditor.s + "/cinematics/faces/null.png" ;
		}

		if (e.command.face != null) {
			s2 = MapEditor.s + "/cinematics/" + e.command.back;
		} else {
			s2 = MapEditor.s + "/cinematics/back.png" ;
		}

		dialogPanel.load(0, new FileHandle(s));
		dialogPanel.load(1, new FileHandle(s2));

		setVisible(true);
	}

	public void update() {
		if (fileOppener != null && fileOppener.isShowing())
			if (fileOppener.fHandle != null) {
				if (fileOppener.type == "faces") {
					dialogPanel.load(0, fileOppener.fHandle);
					fileOppener.dispose();
					repaint();
				} else {
					dialogPanel.load(1, fileOppener.fHandle);
					fileOppener.dispose();
					repaint();
				}

			}
	}

}
