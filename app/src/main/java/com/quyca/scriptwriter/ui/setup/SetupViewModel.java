package com.quyca.scriptwriter.ui.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.model.Play;

public class SetupViewModel extends ViewModel {
    private final MutableLiveData<Play> play = new MutableLiveData<>();


    public void setPlayObservable(Play item) {
        play.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Play> getPlayObservable() {
        if(play.getValue()==null){
            play.setValue(null);
        }
        return play;
    }
}