package com.quyca.scriptwriter.model;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.gson.PlayableAdapter;

import java.util.List;

public class Macro extends PlayableComponent {
    @Expose
    private int position;
    private DocumentFile file;
    private String charColor;
    private String charName;

    public Macro(List<Playable> play) {
        super();
        this.playables = play;
    }

    public static String toJSON(Macro macro) {
        GsonBuilder gsonBuilder = new GsonBuilder();
       /* RuntimeTypeAdapterFactory<Action> adapter =
                RuntimeTypeAdapterFactory
                        .of(Action.class)
                        .registerSubtype(Action.class)
                        .registerSubtype(SoundAction.class);

        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapterFactory(adapter)
                .excludeFieldsWithoutExposeAnnotation().create();*/
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(macro);
    }

    public static Macro parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        /*RuntimeTypeAdapterFactory<Playable> adapter =
               RuntimeTypeAdapterFactory
                        .of(Playable.class)
                        .registerSubtype(Action.class)
                        .registerSubtype(SoundAction.class);*/
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                //.registerTypeAdapterFactory(adapter)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Macro.class);
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public DocumentFile getFile() {
        return file;
    }

    public void setFile(DocumentFile file) {
        this.file = file;
    }

    public String getCharColor() {
        return charColor;
    }

    public void setCharColor(String charColor) {
        this.charColor = charColor;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    @Override
    public String toString() {
        return super.toString() + "\nScene{" +
                "sceneName='" + name + '\'' +
                ", playables=" + playables.size() +
                ", position=" + position +
                '}';
    }
}
