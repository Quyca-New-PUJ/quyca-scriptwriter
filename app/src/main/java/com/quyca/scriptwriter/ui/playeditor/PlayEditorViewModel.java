package com.quyca.scriptwriter.ui.playeditor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.utils.PlayableBundle;

import java.util.List;

/**
 * The type Play editor view model gathers all the objects that should be subscribed to, to organize the different macro before executing the play
 */
public class PlayEditorViewModel extends ViewModel {

    private final MutableLiveData<List<PlayableBundle>> selected = new MutableLiveData<>();
    private final MutableLiveData<List<PlayableBundle>> active = new MutableLiveData<>();

    /**
     * Gets the selected macros.
     *
     * @return the selected macros.
     */
    public MutableLiveData<List<PlayableBundle>> getSelected() {
        return selected;
    }

    /**
     * Gets the active macros
     *
     * @return the active macros
     */
    public MutableLiveData<List<PlayableBundle>> getActive() {
        return active;
    }

    /**
     * Set the selected macros.
     *
     *
     * @param list the selected macros list
     */
    public void  setSelected(List<PlayableBundle> list){
        selected.setValue(list);
    }

    /**
     * Set the active macros.
     *
     * @param list the list
     */
    public void  setActive(List<PlayableBundle> list){
        active.setValue(list);
    }
}
