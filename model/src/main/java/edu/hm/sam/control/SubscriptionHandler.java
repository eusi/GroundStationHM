package edu.hm.sam.control;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Handler for a subscription to a topic
 *
 * @param <T> Submessage extended from Message
 * @author Christoph Kapinos
 * @author Sebastian Schuster
 */

public abstract class SubscriptionHandler<T extends Message & Serializable> {
    public abstract void handleSubscription(T subscription);
}
