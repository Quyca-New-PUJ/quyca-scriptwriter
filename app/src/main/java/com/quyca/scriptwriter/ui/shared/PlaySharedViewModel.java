package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;

import java.util.List;

/**
 * The type Shared view model.
 */
public class PlaySharedViewModel extends ViewModel {
    private final MutableLiveData<Play> play = new MutableLiveData<>();
    private final MutableLiveData<List<Macro>> macros = new MutableLiveData<>();
    private final MutableLiveData<List<Macro>> doneActions = new MutableLiveData<>();
    private final MutableLiveData<Boolean> started = new MutableLiveData<>();

    public void setPlayObservable(Play item) {
        play.setValue(item);
    }


    public LiveData<Play> getPlayObservable() {
        return play;
    }

    public void setActionListObservable( List<Macro> item) {
        doneActions.setValue(item);
    }

    public void setToDoActionsObservable(List<Macro> item) {
        macros.setValue(item);

    }
    public LiveData<List<Macro>> getToDoActionsObservable() {
        return macros;
    }


    public LiveData<List<Macro>> getActionListObservable() {
        return doneActions;
    }
}