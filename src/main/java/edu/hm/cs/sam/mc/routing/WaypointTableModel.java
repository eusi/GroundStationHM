package edu.hm.cs.sam.mc.routing;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;

/**
 * Table model for the waypoints table in the routes view
 * @author Stefan Hoelzl
 *
 */

@SuppressWarnings("serial")
public class WaypointTableModel extends AbstractTableModel {
    private static final Logger LOG = Logger.getLogger(WaypointTableModel.class.getName());

    private SamType sam_type;
    private Zone zone;

    private static final int LAT_ROW = 0;
    private static final int LNG_ROW = 1;
    private static final int COL_CNT = 2;

    public void setSamType(final SamType t) {
        sam_type = t;
        zone = Data.getZones(sam_type);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return COL_CNT;
    }

    @Override
    public int getRowCount() {
        if (zone == null) {
            return 1;
        }
        return zone.getWaypoints().length + 1;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (zone == null) {
            return 0;
        }
        if (rowIndex >= zone.getWaypoints().length) {
            return 0;
        }
        if (columnIndex == LAT_ROW) {
            return zone.getWaypoints()[rowIndex].getLat();
        } else if (columnIndex == LNG_ROW) {
            return zone.getWaypoints()[rowIndex].getLng();
        }
        return 0;
    }

    @Override
    public void setValueAt(final Object val, final int row, final int column) {
        double value = 0;
        boolean valid = true;

        if (zone == null) {
            double lng = 0;
            double lat = 0;

            if (column == LAT_ROW) {
                lat = value;
            } else {
                lng = value;
            }

            final LocationWp lwp = new LocationWp(lat, lng);
            final LocationWp[] lwps = {lwp};
            zone = new Zone(lwps, sam_type, Color.GREEN);
            Data.setZones(zone);
        }

        try {
            value = NumberFormat.getNumberInstance(Locale.US).parse(val.toString()).doubleValue();
        } catch (final Exception e) {
            valid = false;
            LOG.error("Error", e);
        }

        if (valid) {
            if ((column == LAT_ROW) && ((value > 90) || (value < -90))) {
                valid = false;
            } else if ((column == LNG_ROW) && ((value > 180) || (value < -180))) {
                valid = false;
            }
        }

        if (valid) {
            if (valid && (row == zone.getWaypoints().length)) {
                final LocationWp[] list = Arrays.copyOf(zone.getWaypoints(), row + 1);
                final LocationWp wp = new LocationWp(0, 0, 0);
                list[row] = wp;
                zone.setWaypoints(list);
                fireTableDataChanged();
            }

            if (column == LAT_ROW) {
                zone.getWaypoints()[row].setLat(value);
            } else if (column == LNG_ROW) {
                zone.getWaypoints()[row].setLng(value);
            }
        }
        fireTableCellUpdated(row, column);
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }

    @Override
    public String getColumnName(final int colIndex) {
        if (colIndex == LAT_ROW) {
            return "Latitude";
        }
        if (colIndex == LNG_ROW) {
            return "Longitude";
        }
        return "";
    }

}
