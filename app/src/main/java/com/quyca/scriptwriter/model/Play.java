package com.quyca.scriptwriter.model;

import androidx.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Play extends PlayableComponent implements Serializable {
    private List<PlayCharacter> characters;
    private String uriString;

    public static String toJson(Play play) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(play);
    }

    public static Play parseJson(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Play.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlayCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(List<PlayCharacter> characters) {
        this.characters = characters;
    }

    private void orderScenes() {
        List<Playable> scList = new ArrayList<>();
        TreeMap<Integer, Playable> sceneSet = new TreeMap<>();
        playables.forEach(scene -> sceneSet.put(((Scene)scene).getPosition(), scene));
        sceneSet.keySet().forEach(key -> {
            scList.add(sceneSet.get(key));
        });
        playables = scList;
    }

    public List<Playable> getPlayGraph() {
        List<Playable> graph = new ArrayList<>();
        playables.forEach(scene -> graph.addAll(((PlayableComponent)scene).getPlayables()));
        return graph;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    @NonNull
    @Override
    public String toString() {
        return "Play{" +
                "name='" + name + '\'' +
                ", characters=" + characters.size() +
                ", scenes=" + playables.size() +
                '}';
    }
}
