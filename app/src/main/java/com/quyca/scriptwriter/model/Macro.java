package com.quyca.scriptwriter.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.gson.PlayableAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Macro.
 */
public class Macro extends PlayableComponent {
    @Expose
    private int position;
    private DocumentFile file;
    private String charColor;
    private String charName;

    /**
     * Instantiates a new Macro.
     *
     * @param play the list of playables related to the macro.
     */
    public Macro(List<Playable> play) {
        super();
        this.playables = play;
    }

    /**
     * macro to json string.
     *
     * @param macro the macro object
     * @return the string representation
     */
    public static String toJSON(Macro macro) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(macro);
    }

    /**
     * Parse json macro from string representation.
     *
     * @param response the string representation
     * @return the macro object
     */
    public static Macro parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Macro.class);
    }


    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public DocumentFile getFile() {
        return file;
    }

    /**
     * Sets file.
     *
     * @param file the file
     */
    public void setFile(DocumentFile file) {
        this.file = file;
    }

    /**
     * Gets char color.
     *
     * @return the char color
     */
    public String getCharColor() {
        return charColor;
    }

    /**
     * Sets char color.
     *
     * @param charColor the char color
     */
    public void setCharColor(String charColor) {
        this.charColor = charColor;
    }

    /**
     * Gets char name.
     *
     * @return the char name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Sets char name.
     *
     * @param charName the char name
     */
    public void setCharName(String charName) {
        this.charName = charName;
    }

    /**
     * Get resources set.
     *
     * @return the set
     */
    public Set<String> getResources(){
        Set<String> set = new HashSet<>();
        playables.forEach(playable -> {
            Action action = (Action) playable;
            set.addAll(action.getAction().getUsedResources());
        });
        return set;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "\nMacro{" +
                "MacroName='" + name + '\'' +
                ", playables=" + playables.size() +
                ", position=" + position +
                '}';
    }
}
