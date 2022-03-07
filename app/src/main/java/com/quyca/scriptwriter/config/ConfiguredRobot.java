package com.quyca.scriptwriter.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguredRobot implements Serializable {
    private boolean hasExtraServo;
    private boolean hasScreen;
    private int screenSize;
    private Map<String, Map<String, Float>> paramsConfig;

    public ConfiguredRobot() {
    }

    public boolean isHasExtraServo() {
        return hasExtraServo;
    }

    public void setHasExtraServo(boolean hasExtraServo) {
        this.hasExtraServo = hasExtraServo;
    }

    public boolean isHasScreen() {
        return hasScreen;
    }

    public void setHasScreen(boolean hasScreen) {
        this.hasScreen = hasScreen;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    public Map<String, Map<String, Float>> getParamsConfig() {
        return paramsConfig;
    }

    public void setParamsConfig(Map<String, Map<String, Float>> paramsConfig) {
        this.paramsConfig = paramsConfig;
    }

    public Map<String, Float> getParamConfig(String param){
        return paramsConfig.get(param);
    }

    public static ConfiguredRobot parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response, ConfiguredRobot.class);
    }

    public static String toJSON(ConfiguredRobot response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
        Gson gson = gsonBuilder.create();
        return gson.toJson(response);
    }

    public static String getJsonExample(){
        ConfiguredRobot response= new ConfiguredRobot();
        Map<String,Map<String, Float>> defaultParams =new HashMap <> ();
        Map<String, Float> aux =new HashMap <> ();
        aux.put("MAX",1f);
        aux.put("MIN",0f);
        defaultParams.put("perc_nominal_vel", new HashMap<>(aux));
        defaultParams.put("perc_oscilacion_giro",new HashMap<>(aux));
        response.setParamsConfig(defaultParams);
        return toJSON(response);

    }

}
