package edu.hm.cs.sam.mc.misc;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Created by Andreas on 15.01.2015.
 */
public class BuildProperties {
    private String version;
    private String build;
    private String date;
    private String branch;
    private String builtBy;

    /**
     *
     */
    public BuildProperties() {
        final InputStream stream = getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
        final Manifest mf = new Manifest();
        try {
            mf.read(stream);
            final Attributes attributes = mf.getMainAttributes();
            version = attributes.getValue("Implementation-Version");
            build = attributes.getValue("Implementation-Build");
            date = attributes.getValue("Implementation-Timestamp");
            branch = attributes.getValue("Implementation-Branch");
            builtBy = attributes.getValue("Built-By");
        } catch (final IOException e) {
        }
    }

    /**
     * @return branch.
     */
    public String getBranch() {
        if (branch != null) {
            return branch;
        } else {
            return "";
        }
    }

    /**
     * @return build.
     */
    public String getBuild() {
        if (build != null) {
            return build;
        } else {
            return "";
        }
    }

    /**
     * @return builtBy.
     */
    public String getBuiltBy() {
        if (builtBy != null) {
            return builtBy;
        } else {
            return "";
        }
    }

    /**
     * @return date.
     */
    public String getDate() {
        if (date != null) {
            final DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            final Date formatedDate = new Date(Long.parseLong(date));
            return format.format(formatedDate);
        } else {
            return "";
        }
    }

    /**
     * @return version.
     */
    public String getVersion() {
        if (version != null) {
            return version;
        } else {
            return "";
        }
    }
}
