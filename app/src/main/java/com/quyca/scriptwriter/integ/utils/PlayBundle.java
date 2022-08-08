package com.quyca.scriptwriter.integ.utils;

import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;

public class PlayBundle {

    private PlayCharacter charac;
    private Playable playable;

    public PlayBundle(Playable playable, PlayCharacter charac) {
        this.charac = charac;
        this.playable = playable;
    }

    public PlayBundle(Playable playable) {
        this.playable = playable;
    }

    public PlayCharacter getCharac() {
        return charac;
    }

    public void setCharac(PlayCharacter charac) {
        this.charac = charac;
    }

    public Playable getPlayable() {
        return playable;
    }

    public void setPlayable(Playable playable) {
        this.playable = playable;
    }
}
