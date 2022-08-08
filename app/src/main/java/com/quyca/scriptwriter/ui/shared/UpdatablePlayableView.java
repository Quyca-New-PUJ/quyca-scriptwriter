package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;

import java.util.List;

public class UpdatablePlayableView extends ViewModel {
    protected final MutableLiveData<List<Playable>> doneActions = new MutableLiveData<>();

    public void refreshActionListObservable() {
        doneActions.setValue(doneActions.getValue());
    }

    public LiveData<List<Playable>> getActionListObservable() {
        return doneActions;
    }

    public void setActionListObservable(List<Playable> item) {
        doneActions.setValue(item);
    }

}
