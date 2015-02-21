package edu.hm.cs.sam.mc.ir.mainground;

import java.text.DecimalFormat;

import edu.hm.cs.sam.mc.ir.enuminterfaces.WpType;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.MavCmd;

/**
 * Class for infrared and emergent task.
 * Special info added for task internal waypoint comparison.
 * Added own waypoint type enum to LocationWp.
 * 
 * @author Roland Widmann, Maximilian Haag, Markus Linsenmaier, Thomas Angermeier (Team 05 - Infrared/Emergent Task)
 */
public class WPWithInfo extends LocationWp {

	private static final long serialVersionUID = 1L;
	private WpType wayPointType;

    public WPWithInfo(final double lng, final double lat, final double alt, final String ts,
                      final double yaw, final WpType wayPointType) {
        super(lat, lng, alt, ts, yaw);
        this.wayPointType = wayPointType;
    }

    public WPWithInfo(final double lng, final double lat, final double alt, final MavCmd id,
                      final int options, final int p1, final int p2, final int p3, final int p4,
                      final WpType wayPointType) {
        super(lat, lng, alt, id, options, p1, p2, p3, p4);
        this.wayPointType = wayPointType;

    }

    public WPWithInfo(final double lng, final double lat, final double alt,
                      final WpType wayPointType) {
        super(lat, lng, alt);
        this.wayPointType = wayPointType;
    }

  
	public WpType getWayPointType() {
        return wayPointType;
    }

    public void setWayPointType(final WpType wayPointType) {
        this.wayPointType = wayPointType;
    }

    @Override
    public String toString() {
        final DecimalFormat df = new DecimalFormat("0.00000000000");
        final String lng = df.format(super.getLng());
        final String lat = df.format(super.getLat());
        return new StringBuffer(" lng : ").append(lng).append(" lat : ").append(lat)
                .append(" alt : ").append(super.getAlt()).toString();
    }

}
