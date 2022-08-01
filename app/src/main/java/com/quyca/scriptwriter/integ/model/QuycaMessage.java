package com.quyca.scriptwriter.integ.model;

import com.quyca.scriptwriter.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Quyca message.
 */
public class QuycaMessage {

    private String actionId;
    private String charName;
    private String alias;
    private int timestamp;
    private List<Object> params;
    private Action action;

    public QuycaMessage(int timestamp) {
        this.timestamp = timestamp;
        params=new ArrayList<>();
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
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

    // AvanzarCruce 0 1 1
    public String toMessageString(){
        StringBuffer buff = new StringBuffer();
        buff.append(alias);
        buff.append(" ");
        buff.append(actionId);
        buff.append(" ");
        params.forEach(param ->{
            buff.append(param);
            buff.append(" ");
        } );
        buff.append(timestamp);
        //buff.append("\n");
        return buff.toString();
    }
}
