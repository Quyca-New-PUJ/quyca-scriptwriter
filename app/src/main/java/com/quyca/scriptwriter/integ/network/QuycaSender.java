package com.quyca.scriptwriter.integ.network;

import androidx.lifecycle.ViewModel;

import com.quyca.scriptwriter.integ.model.QuycaMessage;
import java.util.List;

/**
 * The interface Quyca sender.
 */
public interface QuycaSender{

    /**
     * Send.
     *
     * @param msg   the msg
     */
    boolean send(List<QuycaMessage> msg);
    boolean send(QuycaMessage msg);
}
