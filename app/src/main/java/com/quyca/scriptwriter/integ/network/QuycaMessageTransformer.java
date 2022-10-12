package com.quyca.scriptwriter.integ.network;

import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.Action;

import java.util.List;

/**
 * The interface Quyca message transformer.
 */
public interface QuycaMessageTransformer {

    /**
     * Create messages list. The method creates a list of related QuycaMessages from a given action.
     *
     * @param action the action
     * @return the list of related QuycaMessages
     */
    List<QuycaMessage> createMessages(Action action);

}
