package com.quyca.scriptwriter.utils;

import com.quyca.scriptwriter.model.Playable;

public class PlayableBundle {
    private Playable playable;
    private boolean selected;
    private boolean selectable;

    public PlayableBundle(Playable playable) {
        this.playable = playable;
        selected =false;
        selectable = true;
    }

    public Playable getPlayable() {
        return playable;
    }

    public void setPlayable(Playable playable) {
        this.playable = playable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
