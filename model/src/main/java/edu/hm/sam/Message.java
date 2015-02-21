package edu.hm.sam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Wrapper for all individual messages of services.
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public abstract class Message {
    private long timestamp;
    private String topic;

    public Message() {
        timestamp = System.currentTimeMillis() / 1000;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] serialize() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            ObjectOutputStream o = new ObjectOutputStream(b);
            o.writeObject(this);
        } catch (IOException e) {
            System.out.println("Serialization failed: " + e.toString());
        }
        return b.toByteArray();
    }

    public static Message deserialize(byte[] bytes) throws IOException, ClassNotFoundException{
        Message message = null;
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        message = (Message) o.readObject();

        return message;

    }
}
