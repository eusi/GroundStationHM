package edu.hm.cs.sam.mc.routing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
import edu.hm.sam.location.Waypoints;
/**
 * Table Model for editing the routes table
 * @author Stefan Hoelzl
 *
 */
@SuppressWarnings("serial")
public class RoutesTableModel extends AbstractTableModel implements Reorderable {

    private final List<Pair<SamType, Boolean>> contentModel = new ArrayList<Pair<SamType, Boolean>>();

    public List<Pair<SamType, Boolean>> getContentModel() {
        return contentModel;
    }
     /**
      * method to init the content model
      */
    public void init() {
        for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("TASK_")) {
                contentModel.add(new Pair<SamType, Boolean>(st, false));
            }
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public int getRowCount() {
        return contentModel.size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return contentModel.get(rowIndex).getValue();
        } else if (columnIndex == 1) {
            return contentModel.get(rowIndex).getKey().toString();
        } else if (columnIndex == 2) {
            final Waypoints wps = Data.getWaypoints(contentModel.get(rowIndex).getKey());
            int length = 0;
            if (wps != null) {
                length = wps.getWaypoints().length;
            }
            return length;
        } else {
            return "";
        }
    }

    @Override
    public String getColumnName(final int colIndex) {
        if (colIndex == 0) {
            return "Submit";
        } else if (colIndex == 1) {
            return "Task";
        } else if (colIndex == 2) {
            return "WPs";
        }
        return "";
    }

    @Override
    public void setValueAt(final Object val, final int row, final int column) {
        if (column == 0) {
            if (Data.getWaypoints(contentModel.get(row).getKey()) == null) {
                JOptionPane.showMessageDialog(null, "ERROR: No Waypoints in selected Route!");
            } else {
                contentModel.get(row).setValue((Boolean) val);
            }
            fireTableCellUpdated(row, column);
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Class<?> getColumnClass(final int column) {
        switch (column) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }

    @Override
    public void reorder(final int fromIndex, int toIndex) {
        final Pair<SamType, Boolean> from = contentModel.get(fromIndex);

        contentModel.remove(fromIndex);
        if(fromIndex < toIndex)  toIndex--;
        contentModel.add(toIndex, from);

        fireTableDataChanged();
    }
    
    /**
     * Method to optimize the order of the selected routes
     * for this a nearest neighboru algorithm is used
     * @param sr indexes of the routes to optimize
     */
    public void optimize(final int[] sr) {
        for (int i = 0; i < (sr.length - 1); i++) {
            double minDist = Double.MAX_VALUE;
            int minIdx = sr[i + 1];

            final Waypoints firstWaypoint = Data.getWaypoints(contentModel.get(sr[i]).getKey());
            final LocationWp firstCoordiante = firstWaypoint.getWaypoints()[firstWaypoint
                    .getWaypoints().length - 1];

            for (int j = i + 1; j < sr.length; j++) {
                final Waypoints secondWaypoint = Data.getWaypoints(contentModel.get(sr[j]).getKey());
                final LocationWp secondCoordinate = secondWaypoint.getWaypoints()[0];

                final double dist = calculateDistanceBetweenTwoCoordsInMeter(firstCoordiante,
                        secondCoordinate);
                if (minDist > dist) {
                    minDist = dist;
                    minIdx = j;
                }
            }

            if ((i + 1) != minIdx) {
                reorder(minIdx, i + 1);
            }
        }
    }
    
    /**
     * Calculates the distance between to given coordinates in meters
     * @param firstCoordiante Coordinate 1
     * @param secondCoordinate Coordinate 2
     * @return distance between coordinates in meters
     */
    private double calculateDistanceBetweenTwoCoordsInMeter(final LocationWp firstCoordiante,
                                                            final LocationWp secondCoordinate) {
        final double earthRadius = 6378.1; // Radius of the earth in km
        final double average = (0.5 - (Math.cos(((secondCoordinate.getLat() - firstCoordiante
                .getLat()) * Math.PI) / 180) / 2))
                + ((Math.cos((firstCoordiante.getLat() * Math.PI) / 180)
                * Math.cos((secondCoordinate.getLat() * Math.PI) / 180) * (1 - Math
                .cos(((secondCoordinate.getLng() - firstCoordiante.getLng()) * Math.PI) / 180))) / 2);
        return earthRadius * 1000 * 2 * Math.asin(Math.sqrt(average)); // in
        // meters
    }
}
