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
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(macro);
    }

    public static Macro parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
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
