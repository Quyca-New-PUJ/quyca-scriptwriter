package com.quyca.scriptwriter.integ.utils;

import android.content.Context;

import com.quyca.scriptwriter.ui.shared.UpdatablePlayableView;

/**
 * The type Ui bundle.It involves all android-related elements pertaining the play.
 */
public class UIBundle {

    private Context context;
    private UpdatablePlayableView model;

    /**
     * Instantiates a new Ui bundle.
     *
     * @param requireContext the required context used to access andoird resources
     * @param mViewModel     the m view model to be updated.
     */
    public UIBundle(Context requireContext, UpdatablePlayableView mViewModel) {
        this.context = requireContext;
        this.model = mViewModel;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public UpdatablePlayableView getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(UpdatablePlayableView model) {
        this.model = model;
    }
}
