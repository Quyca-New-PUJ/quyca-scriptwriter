package com.quyca.scriptwriter.model;

import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.quyca.robotmanager.net.Arc;
import com.quyca.robotmanager.net.PetriNet;
import com.quyca.robotmanager.net.Place;
import com.quyca.robotmanager.net.Transition;
import com.quyca.robotmanager.net.builders.ArcBuilder;
import com.quyca.robotmanager.net.builders.TransitionBuilder;
import com.quyca.robotmanager.network.RobotExecutioner;
import com.quyca.scriptwriter.R;
import com.quyca.scriptwriter.integ.network.QuycaMessageTransformer;
import com.quyca.scriptwriter.integ.utils.NetBundle;
import com.quyca.scriptwriter.integ.utils.UIBundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Scene extends PlayableComponent implements Serializable {
    @Expose
    private int position;
    private String sceneDirName;
    private DocumentFile file;
    private String charColor;
    private String charName;
    private List<PlayUnit> playUnits;
    public Scene(Scene scene) {
        this.name = scene.getName();
        this.position = scene.position;
        this.playables = new ArrayList<>();
        playUnits= new ArrayList<>();
    }

    public static Scene parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Scene.class);

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

    @Override
    public String toString() {
        return "Scene{" +
                "name='" + name + '\'' +
                ", macros=" + playables +
                ", position=" + position +
                ", charColor='" + charColor + '\'' +
                ", charName='" + charName + '\'' +
                '}';
    }

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        TransitionBuilder tBuilder = net.getCreator().createNewTransition();
        ArcBuilder aBuilder = net.getCreator().createNewArc();
        NetBundle netBundle = new NetBundle();

        Transition toPlace = null, fromPlace;
        int last = playUnits.size() - 1;

        NetBundle actBundle;
        List<Place> top;
        List<Place> bottom;
        for (int i = 0; i <= last; i++) {
            actBundle = playUnits.get(i).play(msgCreators, senders, net, bundle);
            top = actBundle.getTopPlaces();
            bottom = actBundle.getBottomPlaces();
            tBuilder.withId(bundle.getContext().getResources().getString(R.string.ACTION_TRANS_NAME, this.getClass().getName(), getName(), i));
            fromPlace = tBuilder.build();
            net.addTransition(fromPlace);
            if (i == 0) {
                netBundle.setTopPlaces(top);
                Transition finalFromPlace = fromPlace;
                bottom.forEach(botPlace -> {
                    aBuilder.fromPlaceToTransition();
                    aBuilder.withPlace(botPlace);
                    aBuilder.withTransition(finalFromPlace);
                    Arc arc = aBuilder.build();
                    net.addArc(arc);
                });
            } else if (i == last) {
                netBundle.setBottomPlaces(bottom);
                Transition finalToPlace = toPlace;
                top.forEach(topPlace -> {
                    aBuilder.withPlace(topPlace);
                    aBuilder.withTransition(finalToPlace);
                    Arc arc = aBuilder.build();
                    net.addArc(arc);
                });
            } else {
                Transition finalToPlace = toPlace;
                top.forEach(topPlace -> {
                    aBuilder.withPlace(topPlace);
                    aBuilder.withTransition(finalToPlace);
                    Arc arc = aBuilder.build();
                    net.addArc(arc);
                });
                Transition finalFromPlace = fromPlace;
                bottom.forEach(botPlace -> {
                    aBuilder.fromPlaceToTransition();
                    aBuilder.withPlace(botPlace);
                    aBuilder.withTransition(finalFromPlace);
                    Arc arc = aBuilder.build();
                    net.addArc(arc);
                });


            }
            toPlace = fromPlace;
        }

        return netBundle;
    }


    public List<PlayUnit> getPlayUnits() {
        return playUnits;
    }

    public void setPlayUnits(List<PlayUnit> playUnits) {
        this.playUnits = playUnits;
    }
}
