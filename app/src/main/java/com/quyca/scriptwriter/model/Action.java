package com.quyca.scriptwriter.model;

import android.util.Log;

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
    /**
     * The Char name.
     */
    @Expose
    protected String charName;
    /**
     * The Alias.
     */
    @Expose
    protected String alias;
    @Expose
    private ConfiguredEmotion emotion;
    @Expose
    private ConfiguredAction action;
    @Expose
    private boolean extra;

    /**
     * Instantiates a new Action.
     */
    public Action() {
        super();
    }


    /**
     * Instantiates a new Action.
     *
     * @param emotion  the emotion
     * @param action   the action
     * @param extra    the extra
     * @param charName the char name
     * @param alias    the alias
     */
    public Action(ConfiguredEmotion emotion, ConfiguredAction action, boolean extra, String charName, String alias) {
        super();
        this.name = action.getActionName();
        this.emotion = emotion;
        this.action = action;
        this.extra = extra;
        this.charName = charName;
        this.alias = alias;
    }

    /**
     * Instantiates a new Action.
     *
     * @param action   the action
     * @param extra    the extra
     * @param charName the char name
     * @param alias    the alias
     */
    public Action(ConfiguredAction action, boolean extra, String charName, String alias) {
        super();
        this.action = action;
        this.name = action.getActionName();
        this.extra = extra;
        this.charName = charName;
        this.alias = alias;
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

    /**
     * Is extra boolean.
     *
     * @return the boolean
     */
    public boolean isExtra() {
        return extra;
    }

    /**
     * Sets extra.
     *
     * @param extra the extra
     */
    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " \nAction{" +
                "emotion=" + emotion +
                ", action=" + action +
                ", resources=" + action.getUsedResources() +
                '}';
    }

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        List<Place> places = new ArrayList<>();
        QuycaMessageTransformer msgCreator = msgCreators.get(alias);
        Log.i("msgCreators1",alias);
        msgCreators.forEach((s, quycaMessageTransformer) -> Log.i("msgCreators1", s));
        RobotExecutioner executioner = senders.get(alias);
        assert msgCreator != null;
        List<QuycaMessage> msgs = msgCreator.createMessages(this);
        msgs.forEach(msg -> {
            PlayViewPlace place = new PlayViewPlace(this, executioner, bundle, msg);
            places.add(place);
            net.addPlace(place);
        });
        Log.i("JLEONHEX CREATION","For action "+action + "actions where created");
        msgs.forEach(msg -> {
            Log.i("JLEONHEX CREATION",msg.toMessageString());
        });
        return new NetBundle(places, places);
    }

    /**
     * It checks if two actions are the same action given an action id.
     *
     * @param otherAction the other action
     * @return true if they are the same action, false otherwise
     */
    public boolean isSameAction(FixedConfiguredAction otherAction) {
        return otherAction.name().equalsIgnoreCase(action.getActionId());
    }
}
