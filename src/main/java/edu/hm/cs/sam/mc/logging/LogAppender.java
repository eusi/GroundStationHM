package edu.hm.cs.sam.mc.logging;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * Log-Appender-class.
 *
 * @author Christoph Friegel
 * @version 0.1
 */

public class LogAppender extends AppenderSkeleton {

    private final JList<String> jList;
    private final DefaultListModel<String> defaultListModel;

    /**
     * formatting logs in list.
     *
     * @param list             list of logs.
     * @param defaultListModel model for list.
     */
    public LogAppender(final JList<String> list, final DefaultListModel<String> defaultListModel) {
        jList = list;
        this.defaultListModel = defaultListModel;
    }

    @Override
    public void append(final LoggingEvent event) {
        defaultListModel.addElement(new String(layout.format(event)));
        jList.ensureIndexIsVisible(defaultListModel.size() - 1);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}