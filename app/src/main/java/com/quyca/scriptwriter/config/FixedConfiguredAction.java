package com.quyca.scriptwriter.config;

/**
 * The enum Fixed configured action. It represents posible fixed actions in the application.
 */
public enum FixedConfiguredAction {
    /**
     * Emotions fixed configured action.
     */
    emotions(false),
    /**
     * Forward fixed configured action.
     */
    forward(true),
    /**
     * Reverse fixed configured action.
     */
    reverse(true),
    /**
     * Right fixed configured action.
     */
    right(true),
    /**
     * Left fixed configured action.
     */
    left(true),
    /**
     * Calibration fixed configured action.
     */
    calibration(true),
    /**
     * Sound fixed configured action.
     */
    sound(false);

    private final boolean answerable;

    FixedConfiguredAction(boolean answerable) {
        this.answerable = answerable;
    }

    /**
     * Is answerable boolean.
     *
     * @return the boolean
     */
    public boolean isAnswerable() {
        return answerable;
    }

    /**
     * Given an action name it determines if it is a fixed action or not.
     *
     * @param s the action name
     * @return true if its in the enum, false otherwise
     */
    public boolean isFixedAction(String s) {

        for (FixedConfiguredAction value : FixedConfiguredAction.values()) {
            if (s.equalsIgnoreCase(value.name())) {
                return true;
            }
        }
        return false;
    }
}
