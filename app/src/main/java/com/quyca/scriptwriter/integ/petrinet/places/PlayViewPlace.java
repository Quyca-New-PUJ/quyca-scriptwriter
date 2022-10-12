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

/**
 * The type Play view place.
 */
public class PlayViewPlace extends PlayPlace {
    /**
     * The action to be represented by the place.
     */
    protected final Playable playable;
    /**
     * The activity Context.
     */
    protected final Activity context;
    /**
     * The graphical component to be updated
     */
    protected final UpdatablePlayableView model;


    /**
     * Instantiates a new Play view place.
     *
     * @param play        the playable that will be represented by the place.
     * @param executioner the executioner
     * @param bundle      the bundle
     * @param msg         the msg to be sent
     */
    public PlayViewPlace(Playable play, RobotExecutioner executioner, UIBundle bundle, Message msg) {
        this.playable = play;
        this.context = (Activity) bundle.getContext();
        this.model = bundle.getModel();
        this.setMsg(msg);
        this.setExecutioner(executioner);
    }

    /**
     * Instantiates a new Play view place.
     *
     * @param play   the playable that will be represented by the place.
     * @param bundle the bundle
     */
    public PlayViewPlace(Playable play, UIBundle bundle) {
        this.playable = play;
        this.context = (Activity) bundle.getContext();
        this.model = bundle.getModel();
    }


    @Override
    public void run() {
        String name = ((QuycaCharacterSender)executioner).getName();
        Log.i(name+" Debug "+playable.getName(),"Starting exec" +msg.toMessageString());
        Log.i(name+" Debug "+playable.getName(),"Answerable: "+msg.shouldRespond());
        super.run();
    }

    /**
     * Refresh ui components according to place status.
     */
    protected void refreshUI() {
        context.runOnUiThread(model::refreshActionListObservable);
    }

    @Override
    public void setStatusToActive() {

        super.setStatusToActive();
        playable.setDone(QuycaCommandState.IN_EXECUTION);
        refreshUI();
    }

    @Override
    public void setStatusToDone() {
        super.setStatusToDone();
        playable.setDone(QuycaCommandState.DONE);
        refreshUI();
    }

    @Override
    public void setStatusToPrepared() {
        super.setStatusToPrepared();
        playable.setDone(QuycaCommandState.TO_EXECUTE);
        refreshUI();
    }

    @Override
    public void setStatusToPaused() {
        super.setStatusToPaused();
        refreshUI();
    }
}
