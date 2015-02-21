package edu.hm.cs.sam.mc.routing;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;

/**
 * Class to Handle Drag-And-Drop in the Routes table
 * @author Stefan Hoelzl
 *
 */
@SuppressWarnings("serial")
public class RouteTransferHandler extends TransferHandler {
	private static final Logger LOG = Logger.getLogger(RouteTransferHandler.class.getName());
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Integer.class,
            DataFlavor.javaJVMLocalObjectMimeType, "Integer Row Index");
    private JTable table = null;

    public RouteTransferHandler(final JTable table) {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(final JComponent c) {
        assert (c == table);
        return new DataHandler(new Integer(table.getSelectedRow()), localObjectFlavor.getMimeType());
    }

    @Override
    public boolean canImport(final TransferHandler.TransferSupport info) {
        final boolean b = (info.getComponent() == table) && info.isDrop()
                && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    public int getSourceActions(final JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(final TransferHandler.TransferSupport info) {
        final JTable target = (JTable) info.getComponent();
        final JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
        final int max = table.getModel().getRowCount();
        if ((index < 0) || (index > max)) {
            index = max;
        }
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            final Integer rowFrom = (Integer) info.getTransferable().getTransferData(
                    localObjectFlavor);
            if ((rowFrom != -1) && (rowFrom != index)) {
                ((Reorderable) table.getModel()).reorder(rowFrom, index);
                if (index > rowFrom) {
                    index--;
                }
                target.getSelectionModel().addSelectionInterval(index, index);
                return true;
            }
        } catch (final Exception e) {
            LOG.error("Error", e);
        }
        return false;
    }

    @Override
    protected void exportDone(final JComponent c, final Transferable t, final int act) {
        if (act == TransferHandler.MOVE) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

}
