package JDBC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class DBTable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list = null;
	private DefaultListModel<String> list_model = null;
	
	public DBTable() {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		setSize(new Dimension(500, 400));
		setBackground(new Color(255, 255, 255, 150));
		setBorder(new LineBorder(Color.RED));
		setVisible(true);
		list_model = new DefaultListModel<>();
		list = new JList<>(list_model);
		list.setBackground(new Color(255, 255, 255, 150));
		list.setFont(new Font("Palatino", Font.BOLD, 20));
		list.setTransferHandler(new ReceiveHanlder());
		list.setPreferredSize(new Dimension(500, 300));
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBackground(new Color(255, 255, 255, 150));
		add(new JLabel("test"), BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		repaint();
	}
}
