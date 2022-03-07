package com.quyca.scriptwriter.ui.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Scene;
import com.quyca.scriptwriter.model.Script;

/**
 * The type Shared view model.
 */
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Script> script = new MutableLiveData<>();
    private final MutableLiveData<Scene> selectedScene = new MutableLiveData<>();
    private final MutableLiveData<PlayCharacter> character = new MutableLiveData<>();
    private final MutableLiveData<Macro> macro = new MutableLiveData<>();
    private final MutableLiveData<Play> play = new MutableLiveData<>();

    public void setPlayObservable(Play item) {
        play.setValue(item);
    }


    public LiveData<Play> getPlayObservable() {
        return play;
    }
    /**
     * Sets script observable.
     *
     * @param item the item
     */
    public void setScriptObservable(Script item) {
        script.setValue(item);
    }

    /**
     * Gets script observable.
     *
     * @return the script observable
     */
    public LiveData<Script> getScriptObservable() {
        if(script.getValue()==null){
            script.setValue(new Script());
        }
        return script;
    }

    public void setSceneObservable(Scene item) {
        selectedScene.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Scene> getSceneObservable() {
        return selectedScene;
    }

    public void setCharacterObservable(PlayCharacter item) {
        character.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<PlayCharacter> getCharacterObservable() {
        if(character.getValue()==null){
            character.setValue(null);
        }
        return character;
    }


    public void setMacroObservable(Macro item) {
        macro.setValue(item);
    }

    /**
     * Gets conf observable.
     *
     * @return the conf observable
     */
    public LiveData<Macro> getMacroObservable() {
        if(macro.getValue()==null){
            macro.setValue(null);
        }
        return macro;
    }
}