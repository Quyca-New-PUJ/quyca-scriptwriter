package com.quyca.scriptwriter.model;

import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Scene implements Serializable {
    @Expose
    private String name;
    @Expose
    private  int position;
    private List<Macro> macros;
    private String sceneDirName;
    private DocumentFile file;
    private String charColor;
    private String charName;

    public Scene(Scene scene) {
        this.name=scene.getName();
        this.position=scene.position;
        this.macros = new ArrayList<>();
    }

    public static Scene parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Scene.class);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Macro> getMacros() {
        return macros;
    }

    public void setMacros(List<Macro> macros) {
        this.macros = macros;
    }


    public String getSceneDirName() {
        return sceneDirName;
    }

    public void setSceneDirName(String sceneDirName) {
        this.sceneDirName = sceneDirName;
    }

    public DocumentFile getFile() {
        return file;
    }

    public void setFile(DocumentFile file) {
        this.file = file;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scene scene = (Scene) o;
        return name.equalsIgnoreCase(scene.name);
    }

    public void orderMacrosPerCharacter() {
        List<Macro> macAux= new ArrayList<>();
        Map<String,List<Macro>> charAux = new HashMap<>();
        Map<String,Integer> charInt = new HashMap<>();

        macros.forEach(macro -> {
            if(!charAux.containsKey(macro.getCharName())){
                charAux.put(macro.getCharName(),new ArrayList<>());
            }
            Objects.requireNonNull(charAux.get(macro.getCharName())).add(macro);
        });
        charAux.keySet().forEach(s -> {
            charInt.put(s,0);
        });
        AtomicInteger macSize= new AtomicInteger(macros.size());
        while(macSize.get() >0){
            charAux.keySet().forEach(s -> {
                List<Macro> macList = charAux.get(s);
                assert macList != null;
                int curSize=charInt.get(s);
                if(macList.size()>curSize){
                    Macro mac= macList.get(curSize);
                    macAux.add(mac);
                    curSize++;
                    charInt.replace(s,curSize);
                    macSize.getAndDecrement();
                }
            });
            Log.i("ALMOSTTHERE",macSize.get()+"");
        }

        macros=macAux;

    }
}
