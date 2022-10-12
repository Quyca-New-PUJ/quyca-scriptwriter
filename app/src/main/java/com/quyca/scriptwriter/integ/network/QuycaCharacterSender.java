package com.quyca.scriptwriter.integ.network;

import android.util.Log;

import com.quyca.robotmanager.network.Message;
import com.quyca.robotmanager.network.RobotActorExecutioner;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.io.IOException;

/**
 * The type Test quyca sender. Class that extends the logic of message sending for robotic actors.
 */
public class QuycaCharacterSender extends RobotActorExecutioner {

    private String name;

    /**
     * Instantiates a new Quyca character sender.
     *
     * @param charac the charac
     * @throws IOException the io exception
     */
    public QuycaCharacterSender(PlayCharacter charac) throws IOException {
        super(charac.getIp(), charac.getPort());
        name = charac.getName();
    }

    @Override
    public boolean sendMessage(Message msg) {
        Log.i("Debug","sendMessage "+ msg.toMessageString());
        return super.sendMessage(msg);
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
}
