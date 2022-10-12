package com.quyca.scriptwriter.utils;

import com.quyca.scriptwriter.model.Playable;

/**
 * The type Playable bundle used to encapsulate playable instances and their status refering to their interaction with the user..
 */
public class PlayableBundle {
    private Playable playable;
    private boolean selected;
    private boolean selectable;

    /**
     * Instantiates a new Playable bundle.
     *
     * @param playable the playable
     */
    public PlayableBundle(Playable playable) {
        this.playable = playable;
        selected =false;
        selectable = true;
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

    /**
     * Gets if the playable is selected.
     *
     * @return the boolean
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets if the playable is selected.
     *
     * @param selected the selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Gets if the playable is selectable.
     *
     * @return the boolean
     */
    public boolean isSelectable() {
        return selectable;
    }

    /**
     * Sets if the playable is selectable.
     *
     * @param selectable the selectable
     */
    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
