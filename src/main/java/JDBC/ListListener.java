package JDBC;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ListListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JList<?>) {
			if (e.getButton() == 3) {
				System.out.println(e.getSource());

				JPopupMenu popupMenu = new JPopupMenu();
				popupMenu.setSize(new Dimension(50, 50));
				popupMenu.setVisible(true);
				
				JMenuItem change_db = new JMenuItem("change to this");
				popupMenu.add(change_db);
				popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				
				change_db.addActionListener(new ActionListener() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void actionPerformed(ActionEvent e1) {
						// TODO Auto-generated method stub
						Conn.changeDB(((JList<DBId>)e.getSource()).getSelectedValue().getDBName());
					}
				});
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
