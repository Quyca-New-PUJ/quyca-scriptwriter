package com.quyca.scriptwriter.integ.network;

import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Playable;
import com.quyca.scriptwriter.model.Script;

import java.util.List;

/**
 * The interface Quyca message transformer.
 */
public interface QuycaMessageTransformer {

    List<QuycaMessage> createMessages(Action action);

}
