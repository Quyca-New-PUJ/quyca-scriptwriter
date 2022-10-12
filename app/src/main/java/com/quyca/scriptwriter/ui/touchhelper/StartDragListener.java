package com.quyca.scriptwriter.ui.touchhelper;

import androidx.recyclerview.widget.RecyclerView;

/**
 * The interface Start drag listener.
 */
public interface StartDragListener {
    /**
     * Request drag.
     *
     * @param viewHolder the view holder
     */
    void requestDrag(RecyclerView.ViewHolder viewHolder);
}