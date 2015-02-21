package edu.hm.sam.searchareaservice;

import java.io.Serializable;

/**
 * Created by Philipp on 28.11.2014.
 * Represents the picture.
 */
public class ByteImage implements Serializable {

    /** bytes of the picture */
    private final byte[] image;

    /**
     * Custom Constructor.
     * @param image the image as byte array
     */
    public ByteImage(final byte[] image) {
        this.image = image;
    }

    /**
     * Returns the image as byte array.
     * @return image as byte array
     */
    public byte[] getImage() {
        return image;
    }
}
