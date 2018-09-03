package JDBC;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

public class DBListDataHandler extends TransferHandler{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Transferable createTransferable(JComponent c) {
		// TODO Auto-generated method stub
		DBId db = null;
		@SuppressWarnings("unchecked")
		JList<DBId> list = (JList<DBId>)c;
		if (c instanceof JList<?>) {
			db = list.getModel().getElementAt(list.getSelectedIndex());
		}
		return new DBListDataTransferable(db.getDBName() + ".db");
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		// TODO Auto-generated method stub
		return COPY;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canImport(TransferSupport support) {
		// TODO Auto-generated method stub
		if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return true;
		}
		else return false;
	}
}
