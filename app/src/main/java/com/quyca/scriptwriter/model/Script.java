package com.quyca.scriptwriter.model;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.quyca.scriptwriter.gson.PlayableAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type Script.
 */
public class Script implements Serializable {
    @Expose
    private List<Action> lines;

    /**
     * Instantiates a new Script.
     */
    public Script(){
        this.lines= new ArrayList<>();
    }

    /**
     * Gets lines.
     *
     * @return the lines
     */
    public List<Action> getLines() {
        return lines;
    }

    /**
     * Sets lines.
     *
     * @param lines the lines
     */
    public void setLines(List<Action> lines) {
        this.lines = lines;
    }

    /**
     * Parse json script.
     *
     * @param response the response
     * @return the script
     */
    public static Script parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Script.class);
    }

    /**
     * To json string.
     *
     * @param response the response
     * @return the string
     */
    public static String toJSON(Script response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Playable.class, new PlayableAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        List<Action> auxList = response.getLines();
        List<Action> actions= new ArrayList<>();
        auxList.forEach(playable -> {
                actions.add(playable);
        });
        response.setLines(actions);
        String jsonReturn = gson.toJson(response);
        response.setLines(auxList);
        return jsonReturn;
    }

    @NonNull
    @Override
    public String toString() {
        return "Script{" +
                "lines=" + lines.size() +
                '}';
    }
}
