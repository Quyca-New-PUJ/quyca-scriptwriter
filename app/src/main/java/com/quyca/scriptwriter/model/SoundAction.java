package com.quyca.scriptwriter.model;

import android.content.Context;
import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.net.Place;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.petrinet.places.PlaySoundPlace;
import com.quyca.scriptwriter.integ.utils.NetBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.utils.AudioRepository;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Sound actio repsents a local action that uses a media file.
 */
public class SoundAction extends Action {

    private FileDescriptor sound;
    @Expose
    private boolean saved;

    /**
     * Instantiates a new Sound action.
     *
     * @param actionsFromId the actions from id
     */
    public SoundAction(ConfiguredAction actionsFromId) {
        super();
        setAction(actionsFromId);
        saved = false;
    }

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        List<Place> places = new ArrayList<>();
        PlaySoundPlace soundPlace = new PlaySoundPlace(this, bundle);
        QuycaMessage msg = new QuycaMessage(QuycaMessageCreator.getNewTimestamp());
        msg.setShouldAnswer(true);
        soundPlace.setMsg(msg);
        places.add(soundPlace);
        net.addPlace(soundPlace);
        return new NetBundle(places, places);
    }

    /**
     * Gets sound file.
     *
     * @param context the context
     * @return the sound file
     * @throws FileNotFoundException the file not found exception
     */
    public Uri getSoundFile(Context context) throws FileNotFoundException {
        return AudioRepository.getAudio(this, context);
    }

    /**
     * Gets sound.
     *
     * @return the sound
     */
    public FileDescriptor getSound() {
        return sound;
    }

    /**
     * Sets sound.
     *
     * @param sound the sound
     */
    public void setSound(FileDescriptor sound) {
        this.sound = sound;
    }

    /**
     * Gets name without prefix.
     *
     * @return the name without prefix
     */
    public String getNameWithoutPrefix() {
        return name.split(".mp3")[0];
    }

    /**
     * Is saved boolean.
     *
     * @return the boolean
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Sets saved.
     *
     * @param saved the saved
     */
    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
