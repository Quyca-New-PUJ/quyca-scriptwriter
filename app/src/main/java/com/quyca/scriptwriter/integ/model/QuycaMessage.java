package com.quyca.scriptwriter.integ.model;

import android.util.Log;

import com.quyca.robotmanager.network.Message;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Quyca message. QuycaMessage class used to wrap the messages that must be sent to the robots.
 */
public class QuycaMessage extends Message{

    private String actionId;
    private String charName;
    private String alias;
    private List<Object> params;
    private Action action;

    /**
     * Instantiates a new Quyca message.
     *
     * @param timestamp the response ack
     */
    public QuycaMessage(int timestamp) {
        super();
        this.ack = timestamp;
        params = new ArrayList<>();
        shouldAnswer=true;

    }

    /**
     * Gets action id.
     *
     * @return the action id
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Sets action id.
     *
     * @param actionId the action id
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * Gets params.
     *
     * @return the params
     */
    public List<Object> getParams() {
        return params;
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(List<Object> params) {
        this.params = params;
    }

    /**
     * Gets action.
     *
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * Sets action.
     *
     * @param action the action
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Gets char name.
     *
     * @return the char name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Sets char name.
     *
     * @param charName the char name
     */
    public void setCharName(String charName) {
        this.charName = charName;
    }

    /**
     * Gets alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets alias.
     *
     * @param alias the alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
        Log.i("msgLogic",this.alias );
    }

    public boolean isShouldAnswer() {
        return shouldAnswer;
    }


    public void setShouldAnswer(boolean shouldAnswer){
        this.shouldAnswer = shouldAnswer;
    }


    @Override
    public String toMessageString() {
        toMessage();
        return super.toMessageString()+"|";
    }

    private void toMessage() {
        StringBuffer buff = new StringBuffer();
        buff.append(alias);
        buff.append(" ");
        buff.append(actionId);
        buff.append(" ");
        params.forEach(param -> {
            buff.append(param);
            buff.append(" ");
        });
        message=buff.toString();

    }
}
