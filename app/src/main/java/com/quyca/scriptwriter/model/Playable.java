package com.quyca.scriptwriter.model;

import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.utils.NetBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;

import java.util.Map;

/**
 * The type Playable represents am abstract class  from which classes that can be played by a robot inherit from.
 */
public abstract class Playable {
    /**
     * The Command State.
     */
    protected QuycaCommandState done;
    /**
     * The Name of the playable
     */
    @Expose
    protected String name;

    /**
     * Instantiates a new Playable.
     */
    public Playable() {
        done = QuycaCommandState.TO_EXECUTE;
    }

    /**
     * It transforms a playable object into a NetBundle used for the petrinet construction.
     *
     * @param msgCreators the message creators
     * @param senders     the message senders
     * @param net         the net
     * @param bundle      the graphic bundle
     * @return the net bundle
     */
    public abstract NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle);

    /**
     * Gets the command status.
     *
     * @return the command status.
     */
    public QuycaCommandState getDone() {
        return done;
    }

    /**
     * Sets the command status..
     *
     * @param done the command status.
     */
    public void setDone(QuycaCommandState done) {
        this.done = done;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
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
