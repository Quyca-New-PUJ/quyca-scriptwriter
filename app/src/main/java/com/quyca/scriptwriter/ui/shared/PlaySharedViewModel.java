package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.Playable;

import java.util.List;

/**
 * The type Shared view model.
 */
public class PlaySharedViewModel extends UpdatablePlayableView {
    private final MutableLiveData<List<Playable>> doneActions = new MutableLiveData<>();
    private final MutableLiveData<Play> play = new MutableLiveData<>();
    private final MutableLiveData<List<Playable>> macros = new MutableLiveData<>();

    public LiveData<Play> getPlayObservable() {
        return play;
    }

    public void setPlayObservable(Play item) {
        play.setValue(item);
    }

    public LiveData<List<Playable>> getToDoActionsObservable() {
        return macros;
    }

    public void setToDoActionsObservable(List<Playable> item) {
        macros.setValue(item);

    }



}