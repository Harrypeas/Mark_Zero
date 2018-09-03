package JDBC;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class DBListDataTransferable implements Transferable {
	private String db_String = null;
	
	public  DBListDataTransferable(String dbId) {
		// TODO Auto-generated constructor stub
		db_String = dbId;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return new DataFlavor[]{DataFlavor.stringFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		if (flavor.equals(DataFlavor.stringFlavor)) {
			return true;
		}
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		if (flavor.equals(DataFlavor.stringFlavor)) {
			return db_String;
		}
		else 
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}

}
