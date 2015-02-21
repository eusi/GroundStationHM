package edu.hm.sam.searchareaservice;

import java.io.Serializable;

import edu.hm.sam.Message;

/**
 * Created by Philipp on 12.12.2014.
 * Represents a message to verify a target.
 */
public class VerifyTargetMessage extends Message implements Serializable {

	/** */
	private static final long serialVersionUID = -1313300842858931849L;

	/** is successful verified */
	private final boolean isSuccessful;
	/** target task type */
	private final String targetTaskType;

    /**
     * custom constructor.
     * @param isSuccessful is successful verified
     * @param targetTaskType target task type
     */
	public VerifyTargetMessage(final boolean isSuccessful, final String targetTaskType) {
		this.isSuccessful = isSuccessful;
		this.targetTaskType = targetTaskType;
	}

    /**
     * Checks if the target was successful verified.
     * @return true is successful verified, false otherwise
     */
	public boolean isSuccessful() {
		return isSuccessful;
	}

    /**
     * Returns the target task type.
     * @return target task type
     */
	public String getTargetTaskType() {
		return targetTaskType;
	}
}