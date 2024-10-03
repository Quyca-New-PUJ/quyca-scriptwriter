package com.quyca.scriptwriter.integ.model;

import com.quyca.robotmanager.network.Message;
import com.quyca.scriptwriter.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * The QuycaMessage class extends the Message class and is used to encapsulate the data needed
 * to send a command or action request to a robot. It includes details like the action ID,
 * character name, robot alias, and any parameters needed for the action.
 * This class is responsible for building the message string that will be sent to the robot.
 */
public class QuycaMessage extends Message {

    // Unique identifier for the action to be executed
    private String actionId;
    // Name of the character that is associated with the message/action
    private String charName;
    // Alias of the robot receiving the message
    private String alias;
    // List of parameters required for the action
    private List<Object> params;
    // Action object that holds detailed configuration for the action
    private Action action;

    /**
     * Constructor that initializes the message with a timestamp (used for acknowledgment).
     * Also initializes the parameters list and sets the message to expect a response.
     *
     * @param timestamp the timestamp to be used as acknowledgment for the message
     */
    public QuycaMessage(int timestamp) {
        super();
        this.ack = timestamp; // Stores the timestamp for message acknowledgment
        params = new ArrayList<>(); // Initializes the parameters list
        shouldAnswer = true; // Specifies that a response is expected by default
    }

    // Getter for the action ID (unique identifier of the action)
    public String getActionId() {
        return actionId;
    }

    // Setter for the action ID
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    // Getter for the list of parameters
    public List<Object> getParams() {
        return params;
    }

    // Setter for the list of parameters
    public void setParams(List<Object> params) {
        this.params = params;
    }

    // Getter for the action object, which holds the detailed action information
    public Action getAction() {
        return action;
    }

    // Setter for the action object
    public void setAction(Action action) {
        this.action = action;
    }

    // Getter for the character name associated with the message
    public String getCharName() {
        return charName;
    }

    // Setter for the character name
    public void setCharName(String charName) {
        this.charName = charName;
    }

    // Getter for the alias of the robot receiving the message
    public String getAlias() {
        return alias;
    }

    // Setter for the alias of the robot
    public void setAlias(String alias) {
        this.alias = alias;
    }

    // Determines whether the message expects a response
    public boolean isShouldAnswer() {
        return shouldAnswer;
    }

    // Sets whether a response is expected for the message
    public void setShouldAnswer(boolean shouldAnswer) {
        this.shouldAnswer = shouldAnswer;
    }

    /**
     * Converts the message object to a string format, appending a delimiter at the end.
     * This method overrides the superclass's `toMessageString` method.
     *
     * @return The string representation of the message with a trailing "|".
     */
    @Override
    public String toMessageString() {
        toMessage(); // Builds the message content before converting to a string
        return super.toMessageString() + "|"; // Appends a "|" as a message delimiter
    }

    /**
     * Helper method that builds the message string by concatenating the alias, action ID,
     * and the parameters into a single string format, separated by spaces.
     * This string will later be sent as the message content.
     */
    private void toMessage() {
        StringBuffer buff = new StringBuffer(); // StringBuffer for efficient string concatenation
        buff.append(alias); // Add robot alias to the message
        buff.append(" ");
        buff.append(actionId); // Add action ID
        buff.append(" ");
        // Loop through the parameters and append each to the message string
        params.forEach(param -> {
            buff.append(param); // Add each parameter to the message
            buff.append(" ");
        });
        message = buff.toString(); // Set the built string as the message content
    }
}
