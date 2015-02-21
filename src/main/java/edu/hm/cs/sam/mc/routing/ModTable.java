package edu.hm.cs.sam.mc.routing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

/**
 * The RXTable provides some extensions to the default JTable
 *
 * 1) Select All editing - when a text related cell is placed in editing mode
 *    the text is selected. Controlled by invoking a "setSelectAll..." method.
 * 
 */
@SuppressWarnings("serial")
public class ModTable extends JTable
{
	private boolean isSelectAllForMouseEvent = true;
	private boolean isSelectAllForActionEvent = true;
	private boolean isSelectAllForKeyEvent = true;

//
// Constructors
//
    /**
     * Constructs a default <code>RXTable</code> that is initialized with a default
     * data model, a default column model, and a default selection
     * model.
     */
    public ModTable()
    {
        this(null, null, null);
    }

    /**
     * Constructs a <code>RXTable</code> that is initialized with
     * <code>dm</code> as the data model, a default column model,
     * and a default selection model.
     *
     * @param dm        the data model for the table
     */
    public ModTable(TableModel dm)
    {
        this(dm, null, null);
    }

    /**
     * Constructs a <code>RXTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code>
     * as the column model, and a default selection model.
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     */
    public ModTable(TableModel dm, TableColumnModel cm)
    {
        this(dm, cm, null);
    }

    /**
     * Constructs a <code>RXTable</code> that is initialized with
     * <code>dm</code> as the data model, <code>cm</code> as the
     * column model, and <code>sm</code> as the selection model.
     * If any of the parameters are <code>null</code> this method
     * will initialize the table with the corresponding default model.
     * The <code>autoCreateColumnsFromModel</code> flag is set to false
     * if <code>cm</code> is non-null, otherwise it is set to true
     * and the column model is populated with suitable
     * <code>TableColumns</code> for the columns in <code>dm</code>.
     *
     * @param dm        the data model for the table
     * @param cm        the column model for the table
     * @param sm        the row selection model for the table
     */
    public ModTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
    }

    /**
     * Constructs a <code>RXTable</code> with <code>numRows</code>
     * and <code>numColumns</code> of empty cells using
     * <code>DefaultTableModel</code>.  The columns will have
     * names of the form "A", "B", "C", etc.
     *
     * @param numRows           the number of rows the table holds
     * @param numColumns        the number of columns the table holds
     */
    public ModTable(int numRows, int numColumns)
    {
        this(new DefaultTableModel(numRows, numColumns));
    }
//
//  Overridden methods
//
	/*
	 *  Override to provide Select All editing functionality
	 */
	public boolean editCellAt(int row, int column, EventObject e)
	{
		boolean result = super.editCellAt(row, column, e);

		if (isSelectAllForMouseEvent
		||  isSelectAllForActionEvent
		||  isSelectAllForKeyEvent)
		{
			selectAll(e);
		}

		return result;
	}

	/*
	 * Select the text when editing on a text related cell is started
	 */
	private void selectAll(EventObject e)
	{
		final Component editor = getEditorComponent();

		if (editor == null
		|| ! (editor instanceof JTextComponent))
			return;

		if (e == null)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  Typing in the cell was used to activate the editor

		if (e instanceof KeyEvent && isSelectAllForKeyEvent)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  F2 was used to activate the editor

		if (e instanceof ActionEvent && isSelectAllForActionEvent)
		{
			((JTextComponent)editor).selectAll();
			return;
		}

		//  A mouse click was used to activate the editor.
		//  Generally this is a double click and the second mouse click is
		//  passed to the editor which would remove the text selection unless
		//  we use the invokeLater()

		if (e instanceof MouseEvent && isSelectAllForMouseEvent)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					((JTextComponent)editor).selectAll();
				}
			});
		}
	}
}  // End of Class RXTable
