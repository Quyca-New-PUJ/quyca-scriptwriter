package com.quyca.scriptwriter.integ.network;

import androidx.annotation.NonNull;

import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.FixedConfiguredAction;
import com.quyca.scriptwriter.config.QuycaConfiguration;
import com.quyca.scriptwriter.integ.model.QuycaMessage;
import com.quyca.scriptwriter.model.Action;
import com.quyca.scriptwriter.model.PlayCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Quyca message creator.
 */
public class QuycaMessageCreator implements QuycaMessageTransformer{

    private QuycaConfiguration conf;
    private ConfiguredRobot robot;
    private PlayCharacter character;
    private int timestamp;
    public QuycaMessageCreator(PlayCharacter character) {
        this.character=character;
        this.conf = character.getConf();
        this.robot = character.getRobotConf();
        timestamp=0;
    }

    @Override
    public List<QuycaMessage> createMessages(@NonNull Action action) {
        List<QuycaMessage> msg = new ArrayList<>();

        if(!action.isSameAction(FixedConfiguredAction.emotions)){
            QuycaMessage actMsg = new QuycaMessage(getNewTimestamp());
            actMsg.setCharName(character.getName());
            actMsg.setAction(action);
            actMsg.setActionId(action.getAction().getActionId());
            setParamsAfterEmoLogic(action,actMsg);
            msg.add(actMsg);
        }
        //robot.isHasScreen() &&
        if(!action.isExtra()){
           QuycaMessage screenMsg = new QuycaMessage(getNewTimestamp());
           screenMsg.setActionId(FixedConfiguredAction.emotions.name());
            screenMsg.setCharName(character.getName());
            screenMsg.setAction(action);
            ArrayList<Object> params = new ArrayList<>();
            params.add(action.getEmotion().getEmotionId().name());
           screenMsg.setParams(params);
           msg.add(screenMsg);
        }
        return msg;
    }

    private int getNewTimestamp() {
        return timestamp++;
    }

    private void setParamsAfterEmoLogic(@NonNull Action action, @NonNull QuycaMessage actMsg) {
        ArrayList<Object> params = new ArrayList<>();
        if(!action.isExtra()){
            float I = action.getEmotion().getIntensity();
            action.getAction().getParams().forEach(paramName -> {
                Map<String, Float> paramMap = robot.getParamConfig(paramName);
                Float max,min,value = null;
                max = paramMap.get("MAX");
                min = paramMap.get("MIN");
                if(max!=null && min!=null){
                    value = calculateParamValue(max,min,I);
                }
                params.add(value);
            } );
        }
        actMsg.setParams(params);
    }

    private float calculateParamValue(float max, float min, float i) {
        float aux= ((i+1)/2);
        float mid = max-min;
        return (aux*mid)+min;
    }

    public QuycaConfiguration getConf() {
        return conf;
    }

    public void setConf(QuycaConfiguration conf) {
        this.conf = conf;
    }

    public ConfiguredRobot getRobot() {
        return robot;
    }

    public void setRobot(ConfiguredRobot robot) {
        this.robot = robot;
    }
}
