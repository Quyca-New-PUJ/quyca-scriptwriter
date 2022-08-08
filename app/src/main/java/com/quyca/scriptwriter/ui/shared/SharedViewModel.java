package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;

import java.util.ArrayList;

/**
 * The type Shared view model.
 */
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Macro> activeMacro = new MutableLiveData<>();
    private final MutableLiveData<Scene> selectedScene = new MutableLiveData<>();
    private final MutableLiveData<PlayCharacter> character = new MutableLiveData<>();
    private final MutableLiveData<Macro> macro = new MutableLiveData<>();
    private final MutableLiveData<Play> play = new MutableLiveData<>();

    public LiveData<Play> getPlayObservable() {
        return play;
    }

    public void setPlayObservable(Play item) {
        play.setValue(item);
    }

    /**
     * Gets script observable.
     *
     * @return the script observable
     */
    public LiveData<Macro> getScriptObservable() {
        if (activeMacro.getValue() == null) {
            activeMacro.setValue(new Macro(new ArrayList<>()));
        }
        return activeMacro;
    }

    /**
     * Sets script observable.
     *
     * @param item the item
     */
    public void setScriptObservable(Macro item) {
        activeMacro.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Scene> getSceneObservable() {
        return selectedScene;
    }

    public void setSceneObservable(Scene item) {
        selectedScene.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<PlayCharacter> getCharacterObservable() {
        if (character.getValue() == null) {
            character.setValue(null);
        }
        return character;
    }

    public void setCharacterObservable(PlayCharacter item) {
        character.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Macro> getMacroObservable() {
        if (macro.getValue() == null) {
            macro.setValue(null);
        }
        return macro;
    }

    public void setMacroObservable(Macro item) {
        macro.setValue(item);
    }
}