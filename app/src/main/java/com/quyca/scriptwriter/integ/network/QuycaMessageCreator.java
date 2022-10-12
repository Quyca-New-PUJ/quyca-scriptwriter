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
 * The type Quyca message creator. It creates the messages related to an action.
 */
public class QuycaMessageCreator implements QuycaMessageTransformer {

    private final PlayCharacter character;
    private QuycaConfiguration conf;
    private ConfiguredRobot robot;
    private static int timestamp = 0;

    /**
     * Instantiates a new Quyca message creator.
     *
     * @param character the character that will receive the messages.
     */
    public QuycaMessageCreator(PlayCharacter character) {
        this.character = character;
        this.conf = character.getConf();
        this.robot = character.getRobotConf();
    }

    @Override
    public List<QuycaMessage> createMessages(@NonNull Action action) {
        List<QuycaMessage> msg = new ArrayList<>();
        //Create non emotional related actions messages.
        if (!action.isSameAction(FixedConfiguredAction.emotions)) {
            QuycaMessage actMsg = new QuycaMessage(getNewTimestamp());
            actMsg.setCharName(character.getName());
            actMsg.setAlias(character.getRobotConf().getAlias());
            actMsg.setAction(action);
            actMsg.setShouldAnswer(action.getAction().isAnswerable());
            actMsg.setActionId(action.getAction().getActionId());
            setParamsAfterEmoLogic(action, actMsg);
            msg.add(actMsg);
        }
        //If action is not extra it probably has an emotional component.
        if (!action.isExtra()) {
            QuycaMessage screenMsg = new QuycaMessage(getNewTimestamp());
            screenMsg.setActionId(FixedConfiguredAction.emotions.name());
            screenMsg.setAlias(character.getRobotConf().getAlias());
            screenMsg.setCharName(character.getName());
            screenMsg.setAction(action);
            screenMsg.setShouldAnswer(FixedConfiguredAction.emotions.isAnswerable());
            ArrayList<Object> params = new ArrayList<>();
            params.add(action.getEmotion().getEmotionId().name());
            screenMsg.setParams(params);
            msg.add(screenMsg);
        }
        return msg;
    }

    private static int getNewTimestamp() {
        return timestamp++;
    }
    /**
     * Sets the emotional affected variables for an action
     *
     * @param action the action
     * @param actMsg the message to be modified
     */
    private void setParamsAfterEmoLogic(@NonNull Action action, @NonNull QuycaMessage actMsg) {
        ArrayList<Object> params = new ArrayList<>();
        if (!action.isExtra()) {
            float I = action.getEmotion().getIntensity();
            action.getAction().getParams().forEach(paramName -> {
                Map<String, Float> paramMap = robot.getParamConfig(paramName);
                Float max, min, value = null;
                max = paramMap.get("MAX");
                min = paramMap.get("MIN");
                if (max != null && min != null) {
                    value = calculateParamValue(max, min, I);
                }
                params.add(value);
            });
        }
        actMsg.setParams(params);
    }
    /**
     * It calculates the parameter value according to a minimun maximum range.
     *
     * @param max Max limit
     * @param min Min limit
     * @param i Number to be transpolated to min-max range
     */
    private float calculateParamValue(float max, float min, float i) {
        float aux = ((i + 1) / 2);
        float mid = max - min;
        return (aux * mid) + min;
    }

    /**
     * Gets conf.
     *
     * @return the conf
     */
    public QuycaConfiguration getConf() {
        return conf;
    }

    /**
     * Sets conf.
     *
     * @param conf the conf
     */
    public void setConf(QuycaConfiguration conf) {
        this.conf = conf;
    }

    /**
     * Gets robot.
     *
     * @return the robot
     */
    public ConfiguredRobot getRobot() {
        return robot;
    }

    /**
     * Sets robot.
     *
     * @param robot the robot
     */
    public void setRobot(ConfiguredRobot robot) {
        this.robot = robot;
    }
}
