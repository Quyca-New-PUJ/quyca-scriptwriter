package com.quyca.scriptwriter.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.ConfiguredEmotion;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.network.QuycaSender;
import com.quyca.scriptwriter.integ.model.QuycaMessage;

import java.util.List;

/**
 * The type Action.
 */
public class Action extends Playable {
    @Expose
    private ConfiguredEmotion emotion;
    @Expose
    private ConfiguredAction action;
    @Expose
    private boolean extra;
    public Action(){

    }


    public Action(ConfiguredEmotion emotion, ConfiguredAction action, boolean extra) {
        super();
        this.emotion = emotion;
        this.action = action;
        this.extra=extra;
    }

    public Action(ConfiguredAction action, boolean extra) {
        super();
        this.action = action;
        this.extra=extra;

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
        return super.toString()+" \nAction{" +
                "emotion=" + emotion +
                ", action=" + action +
                '}';
    }

    @Override
    public boolean play(QuycaMessageTransformer msgCreator, QuycaSender msgSender, Context context) {
        List<QuycaMessage> msg = msgCreator.createMessages(this);
        return msgSender.send(msg);
    }

    public boolean isSameAction(FixedConfiguredAction otherAction){
        return otherAction.name().equalsIgnoreCase(action.getActionId());

    }
}
