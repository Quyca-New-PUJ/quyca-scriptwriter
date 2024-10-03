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
 * The Action class represents a specific action that can be performed by a character in the play.
 * Each action may be associated with a certain emotion and additional configurations.
 * The class extends Playable, meaning it can be executed within a Petri Net framework.
 */
public class Action extends Playable {
    // Exposed fields for serialization and deserialization
    @Expose
    protected String charName; // The name of the character associated with the action
    @Expose
    private ConfiguredEmotion emotion; // The emotion associated with the action
    @Expose
    private ConfiguredAction action; // The configuration details of the action
    @Expose
    private boolean extra; // Flag indicating if this is an extra action

    /**
     * Default constructor for Action.
     */
    public Action() {
    }

    /**
     * Constructor that allows initializing the action with emotion, configuration, and extra flag.
     *
     * @param emotion  The emotion associated with the action.
     * @param action   The configured action to be performed.
     * @param extra    Indicates if this action is an extra one.
     * @param charName The name of the character performing the action.
     */
    public Action(ConfiguredEmotion emotion, ConfiguredAction action, boolean extra, String charName) {
        super();
        this.name = action.getActionName(); // Set the name of the action based on the configured action
        this.emotion = emotion; // Associate the emotion with the action
        this.action = action; // Set the action's configuration
        this.extra = extra; // Set the extra flag
        this.charName = charName; // Set the character's name
    }

    /**
     * Constructor without emotion. It allows initializing with action configuration and extra flag.
     *
     * @param action   The configured action to be performed.
     * @param extra    Indicates if this action is an extra one.
     * @param charName The name of the character performing the action.
     */
    public Action(ConfiguredAction action, boolean extra, String charName) {
        super();
        this.action = action; // Set the action's configuration
        this.name = action.getActionName(); // Set the name of the action based on the configured action
        this.extra = extra; // Set the extra flag
        this.charName = charName; // Set the character's name
    }

    /**
     * Gets the emotion associated with the action.
     *
     * @return The emotion associated with this action.
     */
    public ConfiguredEmotion getEmotion() {
        return emotion;
    }

    /**
     * Sets the emotion associated with the action.
     *
     * @param emotion The emotion to be associated with this action.
     */
    public void setEmotion(ConfiguredEmotion emotion) {
        this.emotion = emotion;
    }

    /**
     * Gets the configured action.
     *
     * @return The configured action.
     */
    public ConfiguredAction getAction() {
        return action;
    }

    /**
     * Sets the configured action.
     *
     * @param action The action configuration to set.
     */
    public void setAction(ConfiguredAction action) {
        this.action = action;
    }

    /**
     * Checks if this action is marked as extra.
     *
     * @return True if the action is extra, otherwise false.
     */
    public boolean isExtra() {
        return extra;
    }

    /**
     * Sets whether this action is marked as extra.
     *
     * @param extra True if the action is extra, otherwise false.
     */
    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    /**
     * Provides a string representation of the action, including its emotion and configuration.
     *
     * @return A string representation of the action.
     */
    @NonNull
    @Override
    public String toString() {
        return super.toString() + " \nAction{" +
                "emotion=" + emotion +
                ", action=" + action +
                '}';
    }

    /**
     * Plays the action within a Petri Net. This method creates messages for the action
     * and adds them to the Petri Net as places.
     *
     * @param msgCreators A map of message creators, used to generate messages for the action.
     * @param senders     A map of robot executioners, responsible for executing the action.
     * @param net         The Petri Net where the action will be executed.
     * @param bundle      The UI bundle for user interaction during the action.
     * @return A NetBundle containing the created places in the Petri Net.
     */
    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        List<Place> places = new ArrayList<>(); // Create a list to store the places in the Petri Net
        QuycaMessageTransformer msgCreator = msgCreators.get(charName); // Get the message creator for the character
        RobotExecutioner executioner = senders.get(charName); // Get the robot executioner for the character
        assert msgCreator != null;

        // Create messages for the action and add them to the Petri Net
        List<QuycaMessage> msgs = msgCreator.createMessages(this);
        msgs.forEach(msg -> {
            PlayViewPlace place = new PlayViewPlace(this, executioner, bundle, msg);
            places.add(place); // Add the place to the list of places in the Petri Net
            net.addPlace(place); // Add the place to the Petri Net
        });

        return new NetBundle(places, places); // Return a NetBundle containing the places
    }

    /**
     * Checks if this action is the same as another action based on a fixed configuration.
     *
     * @param otherAction The other action to compare.
     * @return True if both actions have the same action ID, otherwise false.
     */
    public boolean isSameAction(FixedConfiguredAction otherAction) {
        return otherAction.name().equalsIgnoreCase(action.getActionId());
    }
}
