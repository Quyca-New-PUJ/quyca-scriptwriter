package com.quyca.scriptwriter.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaSender;

import java.io.Serializable;

public abstract class Playable implements Serializable {
    @Expose
    protected boolean isAgrupable;
    @Expose
    protected QuycaCommandState done;


    protected Playable(){
        done = QuycaCommandState.TO_EXECUTE;
    }
    public abstract boolean play(QuycaMessageCreator msgCreator, QuycaSender msgSender, Context context);

    public boolean isAgrupable() {
        return isAgrupable;
    }

    public void setAgrupable(boolean agrupable) {
        isAgrupable = agrupable;
    }

    /**
     * Gets done.
     *
     * @return the done
     */
    public QuycaCommandState getDone() {
        return done;
    }

    /**
     * Sets done.
     *
     * @param done the done
     */
    public void setDone(QuycaCommandState done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Playable{" +
                "isAgrupable=" + isAgrupable +
                ", done=" + done +
                '}';
    }
}
