package com.quyca.scriptwriter.integ.network;

import android.app.Activity;
import android.content.Context;

import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.execscript.ExecScriptViewModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuycaExecutionManager implements Runnable{

    private final QuycaMessageTransformer msgCreator;
    private final QuycaSender sender;
    private final Context context;
    private final List<Playable> actionList;
    private final AtomicBoolean flag;
    private final AtomicBoolean stop;
    private final ExecScriptViewModel model;

    public QuycaExecutionManager(QuycaMessageTransformer msgCreator, QuycaSender sender, Context context, List<Playable> actionList, ExecScriptViewModel model) {
        this.msgCreator = msgCreator;
        this.sender = sender;
        this.context = context;
        this.actionList = actionList;
        this.model=model;
        stop= new AtomicBoolean();
        stop.set(false);
        flag= new AtomicBoolean();
        flag.set(true);
    }

    @Override
    public void run() {
        int i =0;
        Activity act = (Activity) context;
        while (!stop.get())
        {
            for (; i < actionList.size() && flag.get(); i++) {
                Playable playable = actionList.get(i);
                playable.setDone(QuycaCommandState.IN_EXECUTION);
                act.runOnUiThread(() -> model.setActionListObservable(actionList));
                boolean isOk=playable.play(msgCreator, sender, context);
                playable.setDone(isOk?QuycaCommandState.DONE:QuycaCommandState.ERROR);
                act.runOnUiThread(() -> model.setActionListObservable(actionList));
            }

        }
    }

    public void pause(){
        flag.set(false);
    }

    public void resume(){
        flag.set(true);
    }

    public void stop(){
        sender.closeSender();
        flag.set(false);
        stop.set(true);
    }
}
