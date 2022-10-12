package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.quyca.scriptwriter.model.Macro;

/**
 * The type Exec script view model.
 */
public class ExecScriptViewModel extends UpdatablePlayableView {
    /**
     * The To do actions.
     */
    protected final MutableLiveData<Macro> toDoActions = new MutableLiveData<>();

    /**
     * Gets to do actions observable.
     *
     * @return the to do actions observable
     */
    public LiveData<Macro> getToDoActionsObservable() {
        return toDoActions;
    }

    /**
     * Sets to do actions observable.
     *
     * @param item the item
     */
    public void setToDoActionsObservable(Macro item) {
        toDoActions.setValue(item);
    }

}