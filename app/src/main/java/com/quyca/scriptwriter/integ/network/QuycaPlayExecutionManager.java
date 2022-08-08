package com.quyca.scriptwriter.integ.network;

import android.util.Log;

import com.quyca.robotmanager.net.playnet.PlayManager;
import com.quyca.robotmanager.net.playnet.PlayPetriNet;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.integ.exceptions.NonValidPlayableException;
import com.quyca.scriptwriter.integ.utils.PlayBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.Play;
import com.quyca.scriptwriter.model.PlayCharacter;
import com.quyca.scriptwriter.model.Playable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuycaPlayExecutionManager extends PlayManager implements Runnable {

    private final Map<String, QuycaMessageTransformer> msgCreators;
    private final Map<String, RobotExecutioner> senders;
    private final UIBundle bundle;
    private final PlayBundle playBundle;

    public QuycaPlayExecutionManager(PlayBundle playBundle, UIBundle bundle) {
        super();
        this.playBundle = playBundle;
        this.msgCreators = new HashMap<>();
        this.senders = new HashMap<>();
        this.bundle = bundle;
        this.net = new PlayPetriNet();
    }

    private void startHelpers() throws IOException, NonValidPlayableException {
        Playable playable = playBundle.getPlayable();
        if (playable instanceof Play) {
            Play play = (Play) playable;
            play.getCharacters().forEach(playCharacter -> {
                try {
                    msgCreators.put(playCharacter.getName(), new QuycaMessageCreator(playCharacter));
                    senders.put(playCharacter.getName(), new QuycaCharacterSender(playCharacter));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else if (playable instanceof Macro || playable instanceof Action) {
            PlayCharacter playCharacter = playBundle.getCharac();
            msgCreators.put(playCharacter.getName(), new QuycaMessageCreator(playCharacter));
            senders.put(playCharacter.getName(), new QuycaCharacterSender(playCharacter));
        } else {
            throw new NonValidPlayableException();
        }

    }
    @Override
    public void stop(){
        super.stop();
        senders.forEach((s, executioner) -> {executioner.closeResources();});
    }
    @Override
    public void run() {
        try {
            startHelpers();
            Log.i("Debug","helperStarted");
            Log.i("Debug",playBundle.getPlayable().toString());
            playBundle.getPlayable().play(msgCreators, senders, net, bundle);
            Log.i("Debug","netDone");
            Log.i("Debug",net.toString());
            executeNet();
            Log.i("Debug","ExecutionStarted");
        } catch (IOException | NonValidPlayableException e) {
            e.printStackTrace();
        }
    }
}
