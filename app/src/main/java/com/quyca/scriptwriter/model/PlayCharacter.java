package com.quyca.scriptwriter.model;

import android.net.Uri;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.config.ConfiguredRobot;
import com.quyca.scriptwriter.config.QuycaConfiguration;

import java.io.Serializable;
import java.util.List;

public class PlayCharacter implements Serializable {
    @Expose
    private String name;
    @Expose
    private String ip;
    @Expose
    private QuycaConfiguration conf;
    @Expose
    private ConfiguredRobot robotConf;
    @Expose
    private String color;
    @Expose
    private int port;
    private String imageUri;
    private String basicUri;
    private List<Scene> scenes;

    public static PlayCharacter parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, PlayCharacter.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImageUri() {
        return Uri.parse(imageUri);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getBasicUri() {
        return basicUri;
    }

    public void setBasicUri(String basicUri) {
        this.basicUri = basicUri;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public QuycaConfiguration getConf() {
        return conf;
    }

    public void setConf(QuycaConfiguration conf) {
        this.conf = conf;
    }

    public ConfiguredRobot getRobotConf() {
        return robotConf;
    }

    public void setRobotConf(ConfiguredRobot robotConf) {
        this.robotConf = robotConf;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayCharacter that = (PlayCharacter) o;
        return name.equalsIgnoreCase(that.name);
    }

}
