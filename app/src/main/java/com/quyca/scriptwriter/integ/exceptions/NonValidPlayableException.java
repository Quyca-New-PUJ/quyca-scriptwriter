package com.quyca.scriptwriter.integ.exceptions;

/**
 * The type Non valid playable exception.
 */
public class NonValidPlayableException extends RuntimeException {

    /**
     * Instantiates a new Non valid playable exception.
     */
    public NonValidPlayableException() {
        super("This playable is not allowed to be run.");
    }
}
