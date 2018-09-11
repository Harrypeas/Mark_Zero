package JDBC;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class ReceiveHanlder extends TransferHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String db_name;

	@Override
	public boolean canImport(TransferSupport support) {
		// TODO Auto-generated method stub
		
		if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean importData(JComponent comp, Transferable t) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		JList<String> list = (JList<String>) comp;
		DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
		model.clear();
		
		try {
			 db_name =  t.getTransferData(DataFlavor.stringFlavor).toString();
			if (! db_name.contains(".db")) {
				return false;
			}
			else 
			{
				db_name = db_name.split("\\.")[0];
			}
			
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Connection connection = Conn.getConn();
						if (db_name.contains(Conn.getCurrentDBName()) == false) {
							Conn.changeDB(db_name);
						}
						String sql = "show tables;";
						Statement statement = connection.createStatement();
						ResultSet rSet = statement.executeQuery(sql);
						while(rSet.next())
						{
							model.addElement(rSet.getString(1));
						}
						statement.close();
					} catch (SQLException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
			return true;
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}	
}
