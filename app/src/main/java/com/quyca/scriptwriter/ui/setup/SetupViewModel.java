package com.quyca.scriptwriter.ui.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Play;

/**
 * The type Setup view model.
 */
public class SetupViewModel extends ViewModel {
    private final MutableLiveData<Play> play = new MutableLiveData<>();

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Play> getPlayObservable() {
        if (play.getValue() == null) {
            play.setValue(null);
        }
        return play;
    }

    /**
     * Sets play observable.
     *
     * @param item the item
     */
    public void setPlayObservable(Play item) {
        play.setValue(item);
    }
}