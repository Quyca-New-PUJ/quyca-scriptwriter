package com.quyca.scriptwriter.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Play implements Serializable {
    @Expose
    private String name;
    private List<PlayCharacter> characters;
    private List<Scene> scenes;
    private String uriString;

    public static String toJson(Play play) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(play);

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

    public static Play parseJson(String response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Play.class);

    }
    private void orderScenes(){
        List<Scene> scList=new ArrayList<>();
        TreeMap<Integer,Scene> sceneSet = new TreeMap<>();
        scenes.forEach(scene -> sceneSet.put(scene.getPosition(),scene));
        sceneSet.keySet().forEach(key -> {
            scList.add(sceneSet.get(key));
        });
        scenes=scList;

    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public List<Macro> getPlayGraph(){
        List<Macro> graph = new ArrayList<>();
        scenes.forEach(scene -> graph.addAll(scene.getMacros()));
        return graph;
    }

    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    @Override
    public String toString() {
        return "Play{" +
                "name='" + name + '\'' +
                ", characters=" + characters.size() +
                '}';
    }
}
