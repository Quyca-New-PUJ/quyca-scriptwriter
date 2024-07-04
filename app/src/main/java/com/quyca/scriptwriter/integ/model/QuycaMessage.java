package com.quyca.scriptwriter.integ.model;

import com.quyca.robotmanager.network.Message;
import com.quyca.scriptwriter.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Quyca message.
 */
public class QuycaMessage extends Message{

    private String actionId;
    private String charName;
    private String alias;
    private List<Object> params;
    private Action action;

    public QuycaMessage(int timestamp) {
        super();
        this.ack = timestamp;
        params = new ArrayList<>();
        shouldAnswer=true;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    // AvanzarCruce 0 1 1
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
