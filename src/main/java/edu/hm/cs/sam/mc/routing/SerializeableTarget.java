package edu.hm.cs.sam.mc.routing;

import java.io.Serializable;

import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
/**
 * Class to serialize Targets to save and load them
 * @author Stefan Hoelzl
 *
 */
public class SerializeableTarget implements Serializable {
	private static final long serialVersionUID = 6439661867608803784L;
    private final double lat;
    private final double lng;
    private final SamType type;
    
    /**
     * Copy values from given zones to local members
     * @param t given target
     */
    public SerializeableTarget(final Target t) {
        type = t.getSAMType();
        lat = t.getWaypoint().getLat();
        lng = t.getWaypoint().getLng();
    }
    
    /**
     * convert serializeable target to regular target
     * @return regular target
     */
    public Target toTarget() {
        final LocationWp wp = new LocationWp(lat, lng);
        return new Target(wp, type);
    }
}
