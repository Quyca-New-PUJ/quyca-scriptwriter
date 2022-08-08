package com.quyca.scriptwriter.model;

import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.utils.NetBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;

import java.util.Map;

public abstract class Playable {
    protected QuycaCommandState done;
    @Expose
    protected String name;

    public Playable() {
        done = QuycaCommandState.TO_EXECUTE;
    }

    public abstract NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle);

    /**
     * Gets done.
     *
     * @return the done
     */
    public QuycaCommandState getDone() {
        return done;
    }

    /**
     * Sets done.
     *
     * @param done the done
     */
    public void setDone(QuycaCommandState done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Playable{" +
                ", done=" + done +
                '}';
    }
}
