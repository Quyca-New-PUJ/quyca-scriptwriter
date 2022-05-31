package com.quyca.scriptwriter.integ.network;

import android.app.Activity;
import android.content.Context;

import com.quyca.scriptwriter.model.Macro;
import com.quyca.scriptwriter.model.QuycaCommandState;
import com.quyca.scriptwriter.ui.shared.PlaySharedViewModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuycaPlayExecutionManager implements Runnable{

    private final Map<String,QuycaMessageCreator> msgCreators;
    private final QuycaSender sender;
    private final Context context;
    private final List<Macro> macroList;
    private final AtomicBoolean flag;
    private final AtomicBoolean stop;
    private final PlaySharedViewModel model;

    public QuycaPlayExecutionManager(Map<String,QuycaMessageCreator> msgCreator, QuycaSender sender, Context context, List<Macro> actionList, PlaySharedViewModel model) {
        this.msgCreators = msgCreator;
        this.sender = sender;
        this.context = context;
        this.macroList = actionList;
        this.model=model;
        stop= new AtomicBoolean();
        stop.set(false);
        flag= new AtomicBoolean();
        flag.set(true);
    }

    @Override
    public void run() {
        int i =0;
        Activity act = ((Activity)context);
        while (!stop.get()) {
            for (; i < macroList.size() && flag.get(); i++) {
                Macro playable = macroList.get(i);
                playable.setDone(QuycaCommandState.IN_EXECUTION);
                act.runOnUiThread(() -> model.setActionListObservable(macroList));
                QuycaMessageCreator msgCreator = msgCreators.get(playable.getCharName());
                boolean isOk=playable.play(msgCreator, sender, context);
                playable.setDone(isOk?QuycaCommandState.DONE:QuycaCommandState.ERROR);
                act.runOnUiThread(() -> model.setActionListObservable(macroList));
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
        flag.set(false);
        stop.set(true);
        sender.closeSender();

    }

}
