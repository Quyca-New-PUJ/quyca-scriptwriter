package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Playable;

import java.util.List;

/**
 * The type Updatable playable view.
 */
public class UpdatablePlayableView extends ViewModel {
    /**
     * The Done actions.
     */
    protected final MutableLiveData<List<Playable>> doneActions = new MutableLiveData<>();

    /**
     * Refresh action list observable.
     */
    public void refreshActionListObservable() {
        doneActions.setValue(doneActions.getValue());
    }

    /**
     * Gets action list observable.
     *
     * @return the action list observable
     */
    public LiveData<List<Playable>> getActionListObservable() {
        return doneActions;
    }

    /**
     * Sets action list observable.
     *
     * @param item the item
     */
    public void setActionListObservable(List<Playable> item) {
        doneActions.setValue(item);
    }

}
