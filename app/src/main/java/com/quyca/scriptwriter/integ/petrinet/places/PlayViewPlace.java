package com.quyca.scriptwriter.integ.petrinet.places;

import android.app.Activity;
import android.util.Log;

import com.quyca.robotmanager.net.playnet.PlayPlace;
import com.quyca.robotmanager.network.Message;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.integ.network.QuycaCharacterSender;
import com.quyca.scriptwriter.integ.utils.UIBundle;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.shared.UpdatablePlayableView;

public class PlayViewPlace extends PlayPlace {
    protected final Playable playable;
    protected final Activity context;
    protected final UpdatablePlayableView model;


    public PlayViewPlace(Playable play, RobotExecutioner executioner, UIBundle bundle, Message msg) {
        this.playable = play;
        this.context = (Activity) bundle.getContext();
        this.model = bundle.getModel();
        this.setMsg(msg);
        this.setExecutioner(executioner);
    }

    public PlayViewPlace(Playable play, UIBundle bundle) {
        this.playable = play;
        this.context = (Activity) bundle.getContext();
        this.model = bundle.getModel();
    }


    @Override
    public void run() {
        String name = ((QuycaCharacterSender)executioner).name;
        Log.i(name+" Debug "+playable.getName(),"Starting exec" +msg.toMessageString());
        playable.setDone(QuycaCommandState.IN_EXECUTION);
        context.runOnUiThread(model::refreshActionListObservable);
        refreshUI();
        Log.i(name+" Debug "+playable.getName(),"UI Refreshed"+msg.toMessageString());
        Log.i(name+" Debug "+playable.getName(),"Answerable: "+msg.shouldRespond());
        super.run();
        Log.i(name+" Debug "+playable.getName(),"Answer Received"+msg.toMessageString());
        playable.setDone(done ? QuycaCommandState.DONE : QuycaCommandState.ERROR);
        refreshUI();
    }

    protected void refreshUI() {
        context.runOnUiThread(model::refreshActionListObservable);
    }
}
