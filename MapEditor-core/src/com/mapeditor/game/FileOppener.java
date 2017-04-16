package com.mapeditor.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.badlogic.gdx.files.FileHandle;

public class FileOppener extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2433992990105242009L;

	public String type;

	String[] list;

	private JList<String> jList1;

	private JPanel jPanel1;

	private JScrollPane listScrollPane = new JScrollPane();

	JButton b_open = new JButton("Open");

	public String fileName;

	public FileHandle fHandle;

	public FileOppener(String s) {
		type = s;

		this.setResizable(false);
		this.setTitle("Open");
		this.setSize(240, 320);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		jPanel1 = new JPanel();

		jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));

		File dir;

		if (type == "tiles")
			dir = new File(MapEditor.s + "\\data\\tilesets");
		else if (type == "tileset")
			dir = new File(MapEditor.s + "\\tilesets");
		else if (type == "faces")
			dir = new File(MapEditor.s + "\\cinematics\\faces");
		else if (type == "charaset")
			dir = new File(MapEditor.s + "\\characters");
		else if (type == "autotiles")
			dir = new File(MapEditor.s + "\\autotiles");
		else if (type == "cinematics_back")
			dir = new File(MapEditor.s + "\\cinematics");
		else
			dir = new File(MapEditor.s + "\\data\\maps");

		File[] ft = dir.listFiles();

		list = new String[ft.length];

		for (int i = 0; i < ft.length; ++i) {

			if (type == "map") {
				if (!ft[i].isFile()) {
					list[i] = ft[i].getName();
				}
			} else if (type == "teleport") {
				if (!ft[i].isFile()) {
					list[i] = ft[i].getName();
				}
			} else {
				if (ft[i].isFile()) {
					list[i] = ft[i].getName();
				}
			}
		}

		jList1 = new JList<String>();

		jList1.setModel(new AbstractListModel<String>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 5488716905074431525L;
			String[] strings = list;

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jList1.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent evt) {
				// System.out.println(jList1.getSelectedValue());
				fileName = jList1.getSelectedValue();
			}
		});

		listScrollPane.setViewportView(jList1);
		jPanel1.add(listScrollPane);

		b_open.addActionListener(this);

		jPanel1.add(b_open, -1);

		this.add(jPanel1);

		this.pack();

		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (fileName != null) {
			FileHandle fileHandle;
			if (type == "tiles")
				fileHandle = new FileHandle(MapEditor.s + "/data/tilesets/" + fileName);
			else if (type == "tileset")
				fileHandle = new FileHandle(MapEditor.s + "/tilesets/" + fileName);
			else if (type == "characters")
				fileHandle = new FileHandle(MapEditor.s + "/characters/" + fileName);
			else if (type == "faces")
				fileHandle = new FileHandle(MapEditor.s + "/cinematics/faces/" + fileName);
			else if (type == "cinematics_back")
				fileHandle = new FileHandle(MapEditor.s + "/cinematics/" + fileName);
			else if (type == "autotiles")
				fileHandle = new FileHandle(MapEditor.s + "/autotiles/" + fileName);
			else
				fileHandle = new FileHandle(MapEditor.s + "/data/maps/" + fileName);
			fHandle = fileHandle;
		}
		// map.Load(fh);
	}
}