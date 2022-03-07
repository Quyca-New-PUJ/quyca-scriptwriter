package com.quyca.scriptwriter.ui.execscript;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Playable;

import java.util.List;

/**
 * The type Exec script view model.
 */
public class ExecScriptViewModel extends ViewModel{
    private final MutableLiveData<List<Playable>> doneActions = new MutableLiveData<>();
    private final MutableLiveData<List<Playable>> toDoActions = new MutableLiveData<>();

    /**
     * Sets action list observable.
     *
     * @param item the item
     */
    public void setActionListObservable( List<Playable> item) {
        doneActions.setValue(item);
    }

    /**
     * Gets action list observable.
     *
     * @return the action list observable
     */
    public LiveData<List<Playable>> getActionListObservable() {
        return doneActions;
    }


    public void setToDoActionsObservable( List<Playable> item) {
        toDoActions.setValue(item);
    }

    public LiveData<List<Playable>> getToDoActionsObservable(){
        return toDoActions;
    }


}