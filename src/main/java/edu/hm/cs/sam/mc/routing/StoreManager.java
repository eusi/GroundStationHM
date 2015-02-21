package edu.hm.cs.sam.mc.routing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import edu.hm.cs.sam.mc.misc.Data;
import edu.hm.cs.sam.mc.misc.Settings;
import edu.hm.cs.sam.mc.misc.Target;
import edu.hm.cs.sam.mc.misc.Zone;
import edu.hm.sam.location.SamType;

/**
 * Handles the storage and loading of Targets and Zones
 * @author Stefan Hölzl
 *
 */
public class StoreManager {
    private static final Logger LOG = Logger.getLogger(ZoneCreator.class.getName());
	/**
	 * saves all targets and zones
	 */
	public void save() {
		this.saveZones();
		this.saveTargets();
	}
	
	/**
	 * loads all targets and zones
	 */
	public void load() {
		this.loadZones();
		this.loadTargets();
	}
	
	private void saveZones() {
		for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("ZONE_")) {
                if(Data.getZones(st) != null) {
                	this.saveZone(st);
                }
            }
        }
	}
	
	private void loadZones() {
		for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("ZONE_")) {
                this.loadZone(st);
            }
        }
	}
	
	private void saveZone(SamType type) {
        try {
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(Settings.getRtgFldr() + type.toString()
                    + ".ser");
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new SerializeableZone(Data.getZones(type)));
            out.close();
            fileOut.close();
            LOG.info("Zone " + type.toString() + " wurde gespeichert!");
        }
        catch (final Exception e1) {
        	if(e1.getClass() != FileNotFoundException.class) {
        		LOG.error("Zone " + type.toString() + " konnte nicht gespeichert werden!", e1);
        	}
        }
	}
	
	private void loadZone(SamType type) {
		Zone z = null;
        try {
            final FileInputStream fileIn = new FileInputStream(Settings.getRtgFldr()
                    + type.toString() + ".ser");
            final ObjectInputStream in = new ObjectInputStream(fileIn);
            final SerializeableZone sz = (SerializeableZone) in.readObject();
            z = sz.toZone();
            in.close();
            fileIn.close();
        } catch (final Exception e1) {
            z = null;
            if(e1.getClass() != FileNotFoundException.class) {
                LOG.error("Zone " + type.toString() + " konnte nicht geladen werden!", e1);
        	}
        }
        if (z != null) {
            Data.setZones(z);
            LOG.info("Zone " + type.toString() + " wurde geladen!");
        }
	}
	
	private void saveTargets() {
		for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("TARGET_")) {
                if(Data.getTargets(st) != null) {
                	this.saveTarget(st);
                }
            }
        }
	}
	
	private void loadTargets() {
		for (final SamType st : SamType.values()) {
            if (st.toString().startsWith("TARGET_")) {
                this.loadTarget(st);
            }
        }
	}
	
	private void saveTarget(SamType type) {
        try {
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(Settings.getRtgFldr() + type.toString()
                    + ".ser");
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new SerializeableTarget(Data.getTargets(type)));
            out.close();
            fileOut.close();
            LOG.info("Target " + type.toString() + " wurde gespeichert!");
        }
        catch (final Exception e1) {
        	if(e1.getClass() != FileNotFoundException.class) {
        		LOG.error("Target " + type.toString() + " konnte nicht gespeichert werden!", e1);
        	}
        }
	}
	
	private void loadTarget(SamType type) {
		Target t = null;
        try {
            final FileInputStream fileIn = new FileInputStream(Settings.getRtgFldr()
                    + type.toString() + ".ser");
            final ObjectInputStream in = new ObjectInputStream(fileIn);
            final SerializeableTarget st = (SerializeableTarget) in.readObject();
            t = st.toTarget();
            in.close();
            fileIn.close();
        } catch (final Exception e1) {
            t = null;
            if(e1.getClass() != FileNotFoundException.class) {
                LOG.error("Target " + type.toString() + " konnte nicht geladen werden!", e1);
        	}
        }
        if (t != null) {
            Data.setTargets(t);
            LOG.info("Target " + type.toString() + " wurde geladen!");
        }
	}
}
