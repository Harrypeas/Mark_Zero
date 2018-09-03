package JDBC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class JDBC_Choice extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JList<DBId> list;
	private DefaultListModel<DBId> list_model;
	
	private static JLabel current_db;
	
	public JDBC_Choice(ArrayList<DBId> db_names) {
		// TODO Auto-generated constructor stub
		setUndecorated(true);
		setBackground(new Color(255, 255, 255, 0));

		list_model = new DefaultListModel<>();
		list = new JList<>(list_model);
//		list.setPreferredSize(new Dimension(300, 500));
		list.setFixedCellHeight(50);
		list.addMouseListener(new ListListener());
		list.setCellRenderer(new DBListCellRender());
		list.setBackground(new Color(255, 255, 255, 150));
		list.setDragEnabled(true);
		list.setTransferHandler(new DBListDataHandler());
		init_list(db_names);
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		current_db = new JLabel(Conn.getCurrentDBName(), SwingConstants.CENTER);
		current_db.setFont(new Font("Palatino", Font.BOLD, 22));
		current_db.setBackground(new Color(255, 255, 255, 150));
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(current_db, BorderLayout.NORTH);
		JPanel table = new DBTable();
		getContentPane().add(table, BorderLayout.SOUTH);
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void init_list(ArrayList<DBId> db_names)
	{
		list_model.clear();
		if (db_names.size() == 0) {
			JOptionPane.showMessageDialog(null, "There's no Database found!");
			return;
		}
		else {
			for (DBId db_name: db_names)
			{
				list_model.addElement(db_name);
			}
		}
	}
	
	public static void set_current_text(String text)
	{
		current_db.setText(text);
	}
	
	public static void Refresh()
	{
		getFrames()[0].repaint();
	}
}
