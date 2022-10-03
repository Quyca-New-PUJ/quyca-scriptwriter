package com.quyca.scriptwriter.ui.playeditor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.utils.PlayableBundle;

import java.util.List;

public class PlayEditorViewModel extends ViewModel {

    private final MutableLiveData<List<PlayableBundle>> selected = new MutableLiveData<>();
    private final MutableLiveData<List<PlayableBundle>> active = new MutableLiveData<>();

    public MutableLiveData<List<PlayableBundle>> getSelected() {
        return selected;
    }

    public MutableLiveData<List<PlayableBundle>> getActive() {
        return active;
    }

    public void  setSelected(List<PlayableBundle> list){
        selected.setValue(list);
    }

    public void  setActive(List<PlayableBundle> list){
        active.setValue(list);
    }
}
