package com.quyca.scriptwriter;

import static org.junit.Assert.assertEquals;

import com.quyca.scriptwriter.config.ConfiguredAction;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.Macro;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ModelUnitTest {
    @Test
    public void macro_showString() {
        Macro macro = new Macro(new ArrayList<>());
        macro.getPlayables().add(new Action(new ConfiguredAction(FixedConfiguredAction.forward.name(), new ArrayList<>()), true, "hola"));
        System.out.println(Macro.toJSON(macro));
        assertEquals(4, 2 + 2);
    }

    @Test
    public void conf_showString() {
        QuycaConfiguration conf = QuycaConfiguration.getBasicConfig();
        System.out.println(QuycaConfiguration.toJSON(conf));
        assertEquals(4, 2 + 2);
    }

}