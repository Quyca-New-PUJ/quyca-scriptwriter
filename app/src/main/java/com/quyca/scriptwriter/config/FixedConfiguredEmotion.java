package com.quyca.scriptwriter.config;

/**
 * The enum Fixed configured emotion. It represents the emotions a robot can have.
 */
public enum FixedConfiguredEmotion {
    /**
     * Very happy fixed configured emotion.
     */
    very_happy,
    /**
     * Happy fixed configured emotion.
     */
    happy,
    /**
     * Sad fixed configured emotion.
     */
    sad,
    /**
     * Very sad fixed configured emotion.
     */
    very_sad,
    /**
     * Neutral fixed configured emotion.
     */
    neutral,
    /**
     * Angry fixed configured emotion.
     */
    angry,
    /**
     * Sick fixed configured emotion.
     */
    sick,
    /**
     * Surprised fixed configured emotion.
     */
    surprised;

    /**
     * Given an emotion name it determines if it is a fixed action or not.
     *
     * @param s the emotion name
     * @return true if its in the enum, false otherwise
     */
    public boolean isFixedEmotion(String s) {
        for (FixedConfiguredEmotion value : FixedConfiguredEmotion.values()) {
            if (s.equalsIgnoreCase(value.name())) {
                return true;
            }
        }
        return false;
    }
}
