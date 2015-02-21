
package edu.hm.cs.sam.mc.routing;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;

/**
 * Table model for the target table
 * @author Stefan Hoelzl
 *
 */
@SuppressWarnings("serial")
public class TargetTableModel extends AbstractTableModel {

	private final List<Pair<SamType, Boolean>> contentModel = new ArrayList<Pair<SamType, Boolean>>();
	
	private final int CHK_COL = 0;
	private final int NAME_COL = 1;
	private final int LAT_COL = 2;
	private final int LNG_COL = 3;
	private final int COL_CNT = 4;
	
    public List<Pair<SamType, Boolean>> getContentModel() {
        return contentModel;
    }
    
    /**
     * initializes the contentModel
     */
    private void init() {
        for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("TARGET_")) {
                contentModel.add(new Pair<SamType, Boolean>(st, false));
            }
        }
    }
	
    public TargetTableModel() {
    	init();
    }
    
	@Override
	public int getColumnCount() {
		return COL_CNT;
	}

	@Override
	public int getRowCount() {
		return contentModel.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Target t = Data.getTargets(contentModel.get(row).getKey());
		switch(col) {
		case NAME_COL:
			return contentModel.get(row).getKey().toString();
		case CHK_COL:
			return contentModel.get(row).getValue();
		case LNG_COL:
			if(t == null) return 0;
			return t.getWaypoint().getLng();
		case LAT_COL:
			if(t == null) return 0;
			return t.getWaypoint().getLat();
		default:
			return null;
		}
	}
	
	@Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if(columnIndex == LAT_COL || columnIndex == LNG_COL || columnIndex == CHK_COL) {
        	return true;
        }
		return false;
    }
	
	@Override
    public void setValueAt(final Object val, final int row, final int column) {
		Target t = Data.getTargets(contentModel.get(row).getKey());
		if(column == LAT_COL || column == LNG_COL) {
			if(t == null) {
				LocationWp wp = new LocationWp(0, 0);
				t = new Target(wp, contentModel.get(row).getKey());
				Data.setTargets(t);
			}
		}
		double value = 0;
		if(column == LAT_COL || column == LNG_COL) {
			try {
	            value = NumberFormat.getNumberInstance(Locale.US).parse(val.toString()).doubleValue();
	        } catch (final Exception e) {
	            value = 0;
	        }
		}
		
		if(column == CHK_COL) {
			if(t == null) {
				JOptionPane.showMessageDialog(null, "ERROR: No Waypoint to this Target!");
				return;
			}
		}
		
		switch(column) {
		case CHK_COL:
			contentModel.get(row).setValue((boolean)val);
			break;
		case LNG_COL:
			Data.getTargets(contentModel.get(row).getKey()).getWaypoint().setLng(value);
			break;
		case LAT_COL:
			Data.getTargets(contentModel.get(row).getKey()).getWaypoint().setLat(value);
			break;
		}
        fireTableCellUpdated(row, column);
    }
	
	@Override
    public String getColumnName(final int colIndex) {
        if (colIndex == LAT_COL) {
            return "Latitude";
        }
        if (colIndex == LNG_COL) {
            return "Longitude";
        }
        if(colIndex == NAME_COL) {
        	return "Name";
        }
        if(colIndex == CHK_COL) {
        	return "Submit";
        }
        return "";
    }
	
	@Override
    public Class<?> getColumnClass(final int column) {
        switch (column) {
        case CHK_COL:
            return Boolean.class;
        case NAME_COL:
            return String.class;
        case LAT_COL:
            return String.class;
        case LNG_COL:
            return String.class;
        default:
            return String.class;
        }
    }

}
