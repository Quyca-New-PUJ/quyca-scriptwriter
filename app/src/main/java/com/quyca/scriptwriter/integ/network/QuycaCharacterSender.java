package com.quyca.scriptwriter.integ.network;

import android.util.Log;

import com.quyca.robotmanager.network.Message;
import com.quyca.robotmanager.network.RobotActorExecutioner;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.io.IOException;

/**
 * The type Test quyca sender.
 */
public class QuycaCharacterSender extends RobotActorExecutioner {

    public String name;
    public QuycaCharacterSender(PlayCharacter charac) throws IOException {
        super(charac.getIp(), charac.getPort());
        name = charac.getName();
    }

    @Override
    public synchronized boolean sendMessage(Message msg) {
        Log.i("Debug","sendMessage "+ msg.toMessageString());
        return super.sendMessage(msg);
    }
}
