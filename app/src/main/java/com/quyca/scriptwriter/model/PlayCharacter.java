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

/**
 * The type Play character represents a character that can be used in the play's creation.
 */
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

    /**
     * Parse json string into a  play character.
     *
     * @param response the string representation
     * @return the play character object
     */
    public static PlayCharacter parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, PlayCharacter.class);
    }

    /**
     * Gets the name of the character.
     *
     * @return the name of the character.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the character..
     *
     * @param name the name of the character.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets image uri.
     *
     * @return the image uri
     */
    public Uri getImageUri() {
        return Uri.parse(imageUri);
    }

    /**
     * Sets image uri.
     *
     * @param imageUri the image uri
     */
    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }

    /**
     * Sets image uri.
     *
     * @param imageUri the image uri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Gets basic uri related to the character folder.
     *
     * @return the basic uri related to the character folder.
     */
    public String getBasicUri() {
        return basicUri;
    }

    /**
     * Sets basic uri related to the character folder.
     *
     * @param basicUri the basic uri related to the character folder.
     */
    public void setBasicUri(String basicUri) {
        this.basicUri = basicUri;
    }

    /**
     * Gets ip.
     *
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets ip.
     *
     * @param ip the ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Gets scenes.
     *
     * @return the scenes
     */
    public List<Scene> getScenes() {
        return scenes;
    }

    /**
     * Sets scenes.
     *
     * @param scenes the scenes
     */
    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    /**
     * Gets QuycaConfiguration for the character.
     *
     * @return the QuycaConfiguration for the character.
     */
    public QuycaConfiguration getConf() {
        return conf;
    }

    /**
     * Sets QuycaConfiguration for the character..
     *
     * @param conf the QuycaConfiguration for the character.
     */
    public void setConf(QuycaConfiguration conf) {
        this.conf = conf;
    }

    /**
     * Gets the robot configuration.
     *
     * @return the robot configuration
     */
    public ConfiguredRobot getRobotConf() {
        return robotConf;
    }

    /**
     * Sets robot configuration.
     *
     * @param robotConf the robot configuration
     */
    public void setRobotConf(ConfiguredRobot robotConf) {
        this.robotConf = robotConf;
    }

    /**
     * Gets the representative color of the character.
     *
     * @return the representative color of the character
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the representative color of the character.
     *
     * @param color the representative color of the character
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
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
