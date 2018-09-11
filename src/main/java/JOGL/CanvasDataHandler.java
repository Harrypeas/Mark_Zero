package JOGL;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

public class CanvasDataHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean canImport(TransferSupport support) {
		// TODO Auto-generated method stub
		if (support.isDataFlavorSupported(DataFlavor.stringFlavor) || support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean importData(JComponent comp, Transferable t) {
		// TODO Auto-generated method stub
		try {
			DataFlavor[] flavors = t.getTransferDataFlavors();
			for(DataFlavor flavor: flavors)
			{
				if (flavor.equals(DataFlavor.javaFileListFlavor)) {
					System.out.println("data recognized");
					Object file = (Object) t.getTransferData(DataFlavor.javaFileListFlavor);
					System.out.println(file.toString());
					return true;
				}
			}
			System.out.println(flavors.toString());
			String name = (String) t.getTransferData(DataFlavor.stringFlavor);
			JOptionPane.showMessageDialog(null, name);
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
