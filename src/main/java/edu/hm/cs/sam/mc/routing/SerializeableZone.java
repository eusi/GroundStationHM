package edu.hm.cs.sam.mc.routing;

import java.awt.Color;
import java.io.Serializable;

import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.sam.location.LocationWp;
import edu.hm.sam.location.SamType;
/**
 * Class to serialize Zones to save and load them
 * @author Stefan Hoelzl
 *
 */
public class SerializeableZone implements Serializable {
    private static final long serialVersionUID = 3437702292835981289L;
    private final int rgb;
    private final double[][] wps;
    private final SamType type;
    
    /**
     * Copy values from given zones to local members
     * @param z given zone
     */
    public SerializeableZone(final Zone z) {
        rgb = z.getColor().getRGB();
        type = z.getSAMType();
        wps = new double[z.getWaypoints().length][2];
        for (int i = 0; i < z.getWaypoints().length; i++) {
            final LocationWp lwp = z.getWaypoints()[i];
            wps[i][0] = lwp.getLat();
            wps[i][1] = lwp.getLng();
        }
    }
    
    /**
     * convert serializeable zone to regular zone
     * @return regular zone
     */
    public Zone toZone() {
        final Color c = new Color(rgb);
        final LocationWp[] lwps = new LocationWp[wps.length];
        for (int i = 0; i < wps.length; i++) {
            lwps[i] = new LocationWp(wps[i][0], wps[i][1], 0);
        }
        return new Zone(lwps, type, c);
    }
}
