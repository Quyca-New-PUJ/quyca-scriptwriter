package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.quyca.scriptwriter.model.Macro;

/**
 * The type Exec script view model.
 */
public class ExecScriptViewModel extends UpdatablePlayableView {
    protected final MutableLiveData<Macro> toDoActions = new MutableLiveData<>();

    public LiveData<Macro> getToDoActionsObservable() {
        return toDoActions;
    }

    public void setToDoActionsObservable(Macro item) {
        toDoActions.setValue(item);
    }

}