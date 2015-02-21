package edu.hm.sam.logger;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by christoph on 11/12/14.
 */
public class LoggerMessage extends Message implements Serializable {
    public enum Level {
        WARN, INFO, FATAL
    }

    private String message;
    private Level level;

    public LoggerMessage(Level level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }
}
