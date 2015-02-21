package edu.hm.sam;

/**
 * Holding class for message to avoid NetworkOnMainThreadException.
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */
public class MessageHolder {
    public static enum Channel {
        ACTION, RESULT, HEARTBEAT
    }

    private Channel channel;

    private String topic;
    private Message message;

    public MessageHolder(Channel channel, String topic, Message message) {
        this.channel = channel;
        this.topic = topic;
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getTopic() {
        return topic;
    }

    public Message getMessage() {
        return message;
    }
}