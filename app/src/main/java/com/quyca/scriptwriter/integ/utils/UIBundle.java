package com.quyca.scriptwriter.integ.utils;

import android.content.Context;

import com.quyca.scriptwriter.ui.shared.UpdatablePlayableView;

public class UIBundle {

    private Context context;
    private UpdatablePlayableView model;

    public UIBundle(Context requireContext, UpdatablePlayableView mViewModel) {
        this.context = requireContext;
        this.model = mViewModel;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public UpdatablePlayableView getModel() {
        return model;
    }

    public void setModel(UpdatablePlayableView model) {
        this.model = model;
    }
}
