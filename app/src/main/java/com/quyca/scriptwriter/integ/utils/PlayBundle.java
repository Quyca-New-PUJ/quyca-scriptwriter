package com.quyca.scriptwriter.integ.utils;

import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;

/**
 * The type Play bundle. It gathers all the components that involve the net and the robot communication.
 */
public class PlayBundle {

    private PlayCharacter charac;
    private Playable playable;

    /**
     * Instantiates a new Play bundle.
     *
     * @param playable the playable
     * @param charac   the character that is involved in the playable
     */
    public PlayBundle(Playable playable, PlayCharacter charac) {
        this.charac = charac;
        this.playable = playable;
    }

    /**
     * Instantiates a new Play bundle.
     *
     * @param playable the playable
     */
    public PlayBundle(Playable playable) {
        this.playable = playable;
    }

    /**
     * Gets charac.
     *
     * @return the charac
     */
    public PlayCharacter getCharac() {
        return charac;
    }

    /**
     * Sets charac.
     *
     * @param charac the charac
     */
    public void setCharac(PlayCharacter charac) {
        this.charac = charac;
    }

    /**
     * Gets playable.
     *
     * @return the playable
     */
    public Playable getPlayable() {
        return playable;
    }

    /**
     * Sets playable.
     *
     * @param playable the playable
     */
    public void setPlayable(Playable playable) {
        this.playable = playable;
    }
}
