package edu.hm.sam.camera;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by christoph on 08/12/14.
 */
public class CameraMessage extends Message implements Serializable {
    public int getFoo() {
        return foo;
    }

    public void setFoo(int foo) {
        this.foo = foo;
    }

    private int foo;
}
