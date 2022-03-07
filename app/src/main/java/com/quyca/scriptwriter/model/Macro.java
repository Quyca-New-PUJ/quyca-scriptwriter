package com.quyca.scriptwriter.model;

import android.content.Context;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.quyca.scriptwriter.integ.network.QuycaMessageCreator;
import com.quyca.scriptwriter.integ.network.QuycaSender;

import java.util.List;

public class Macro extends Playable {
    @Expose
    private String macroName;
    @Expose
    private List<Action> actions;
    @Expose
    private int position;
    private DocumentFile file;
    private String charColor;
    private String charName;

    public static String toJSON(Macro macro) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        RuntimeTypeAdapterFactory<Action> adapter =
                RuntimeTypeAdapterFactory
                        .of(Action.class)
                        .registerSubtype(Action.class)
                        .registerSubtype(SoundAction.class);

        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapterFactory(adapter)
                .excludeFieldsWithoutExposeAnnotation().create();
        return  gson.toJson(macro);
    }

    public static Macro parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        RuntimeTypeAdapterFactory<Action> adapter =
                RuntimeTypeAdapterFactory
                        .of(Action.class)
                        .registerSubtype(Action.class)
                        .registerSubtype(SoundAction.class);
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapterFactory(adapter)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Macro.class);
    }

    @Override
    public boolean play(QuycaMessageCreator msgCreator, QuycaSender msgSender, Context context) {
        boolean done =true;
        for (Action action : actions) {
            done = done && action.play(msgCreator,msgSender,context);
        }
        return done;
    }

    public Macro(List<Action> play){
        super();
        this.actions =play;
    }

    public String getMacroName() {
        return macroName;
    }

    public void setMacroName(String macroName) {
        this.macroName = macroName;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
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
        return super.toString()+ "\nScene{" +
                "sceneName='" + macroName + '\'' +
                ", playables=" + actions.size() +
                ", position=" + position +
                '}';
    }
}
