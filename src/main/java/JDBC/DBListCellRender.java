package JDBC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

public class DBListCellRender extends JPanel implements ListCellRenderer<DBId>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel db_name = null;
	
	public DBListCellRender() {
		// TODO Auto-generated constructor stub
		db_name = new JLabel("", SwingConstants.CENTER);
		db_name.setOpaque(false);
		db_name.setFont(new Font("Palatino", Font.BOLD, 20));

		setLayout(new BorderLayout());
		add(db_name, BorderLayout.CENTER);
		setBackground(new Color(255, 255, 255, 0));
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends DBId> list, DBId value, int index, boolean isSelected,
			boolean cellHasFocus) {
		// TODO Auto-generated method stub
		String name = value.getDBName();
		if (isSelected) {
			setBackground(new Color(150, 200, 50, 100));
		}
		else if (! isSelected) {
			setBackground(new Color(0, 0, 0, 0));
		}
		db_name.setText(name);
		list.repaint();
		return this;
	}

}
