package com.quyca.scriptwriter.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.net.Place;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.ConfiguredEmotion;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.petrinet.places.PlayViewPlace;
import com.quyca.scriptwriter.integ.utils.NetBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Action.
 */
public class Action extends Playable {
    @Expose
    protected String charName;
    @Expose
    private ConfiguredEmotion emotion;
    @Expose
    private ConfiguredAction action;
    @Expose
    private boolean extra;

    public Action() {

    }


    public Action(ConfiguredEmotion emotion, ConfiguredAction action, boolean extra, String charName) {
        super();
        this.name = action.getActionName();
        this.emotion = emotion;
        this.action = action;
        this.extra = extra;
        this.charName = charName;
    }

    public Action(ConfiguredAction action, boolean extra, String charName) {
        super();
        this.action = action;
        this.name = action.getActionName();
        this.extra = extra;
        this.charName = charName;
    }

    /**
     * Gets emotion.
     *
     * @return the emotion
     */
    public ConfiguredEmotion getEmotion() {
        return emotion;
    }

    /**
     * Sets emotion.
     *
     * @param emotion the emotion
     */
    public void setEmotion(ConfiguredEmotion emotion) {
        this.emotion = emotion;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public ConfiguredAction getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(ConfiguredAction action) {
        this.action = action;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " \nAction{" +
                "emotion=" + emotion +
                ", action=" + action +
                '}';
    }

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        List<Place> places = new ArrayList<>();
        QuycaMessageTransformer msgCreator = msgCreators.get(charName);
        RobotExecutioner executioner = senders.get(charName);
        assert msgCreator != null;
        List<QuycaMessage> msgs = msgCreator.createMessages(this);
        msgs.forEach(msg -> {
            PlayViewPlace place = new PlayViewPlace(this, executioner, bundle, msg);
            places.add(place);
            net.addPlace(place);
        });

        return new NetBundle(places, places);
    }

    public boolean isSameAction(FixedConfiguredAction otherAction) {
        return otherAction.name().equalsIgnoreCase(action.getActionId());
    }
}
