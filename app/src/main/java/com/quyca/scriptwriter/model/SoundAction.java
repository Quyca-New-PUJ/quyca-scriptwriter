package com.quyca.scriptwriter.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.net.Place;
import com.quyca.robotmanager.network.RobotExecutioner;
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

public class SoundAction extends Action {

    private FileDescriptor sound;
    @Expose
    private boolean saved;

    public SoundAction() {
        super();
        saved = false;
    }

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        List<Place> places = new ArrayList<>();
        PlaySoundPlace soundPlace = new PlaySoundPlace(this, bundle);
        places.add(soundPlace);
        net.addPlace(soundPlace);
        return new NetBundle(places, places);
    }

    public FileDescriptor getSoundFile(Context context) throws FileNotFoundException {
        return AudioRepository.getAudio(this, context);
    }

    public FileDescriptor getSound() {
        return sound;
    }

    public void setSound(FileDescriptor sound) {
        this.sound = sound;
    }

    public String getNameWithoutPrefix() {
        return name.split(".mp4")[0];
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
