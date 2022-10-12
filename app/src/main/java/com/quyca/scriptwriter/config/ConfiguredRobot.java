package com.quyca.scriptwriter.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Configured robot.
 */
public class ConfiguredRobot implements Serializable {
    private boolean hasExtraServo;
    private boolean hasScreen;
    private int screenSize;
    private String alias;
    private Map<String, Map<String, Float>> paramsConfig;

    /**
     * Instantiates a new Configured robot.
     */
    public ConfiguredRobot() {
    }

    /**
     * Parse json configured robot.
     *
     * @param response the string representing a robotConf.
     * @return the configured robot object
     */
    public static ConfiguredRobot parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response, ConfiguredRobot.class);
    }

    /**
     * To json string.
     *
     * @param response a configured robot object
     * @return the string JSON representation
     */
    public static String toJSON(ConfiguredRobot response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.toJson(response);
    }

    /**
     * Gets a json example of a Robot conf.
     *
     * @return the json example
     */
    public static String getJsonExample() {
        ConfiguredRobot response = new ConfiguredRobot();
        Map<String, Map<String, Float>> defaultParams = new HashMap<>();
        Map<String, Float> aux = new HashMap<>();
        aux.put("MAX", 1f);
        aux.put("MIN", 0f);
        defaultParams.put("perc_nominal_vel", new HashMap<>(aux));
        defaultParams.put("perc_oscilacion_giro", new HashMap<>(aux));
        response.setParamsConfig(defaultParams);
        return toJSON(response);

    }

    /**
     * Is has extra servo boolean.
     *
     * @return true if it has an extraServo
     */
    public boolean isHasExtraServo() {
        return hasExtraServo;
    }

    /**
     * Sets has extra servo.
     *
     * @param hasExtraServo true if it has an extraServo
     */
    public void setHasExtraServo(boolean hasExtraServo) {
        this.hasExtraServo = hasExtraServo;
    }

    /**
     * Is has screen boolean.
     *
     * @return true if it has an screen
     */
    public boolean isHasScreen() {
        return hasScreen;
    }

    /**
     * Sets has screen.
     *
     * @param hasScreen true if it has an screen
     */
    public void setHasScreen(boolean hasScreen) {
        this.hasScreen = hasScreen;
    }

    /**
     * Gets screen size.
     *
     * @return the screen size
     */
    public int getScreenSize() {
        return screenSize;
    }

    /**
     * Sets screen size.
     *
     * @param screenSize the screen size
     */
    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * Gets the action params config.
     *
     * @return the action params config for the robot
     */
    public Map<String, Map<String, Float>> getParamsConfig() {
        return paramsConfig;
    }

    /**
     * Sets params config.
     *
     * @param paramsConfig the params config
     */
    public void setParamsConfig(Map<String, Map<String, Float>> paramsConfig) {
        this.paramsConfig = paramsConfig;
    }

    /**
     * Gets alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets alias.
     *
     * @param alias the alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gets param config.
     *
     * @param param the param
     * @return the param config
     */
    public Map<String, Float> getParamConfig(String param) {
        return paramsConfig.get(param);
    }

}
