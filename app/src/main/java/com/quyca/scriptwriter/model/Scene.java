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

/**
 * The type Scene is composed of multiple playables, specially macros.
 */
public class Scene extends PlayableComponent implements Serializable {
    @Expose
    private int position;
    private String sceneDirName;
    private DocumentFile file;
    private String charColor;
    private String charName;
    private List<PlayUnit> playUnits;

    /**
     * Instantiates a new Scene.
     *
     * @param scene the scene
     */
    public Scene(Scene scene) {
        this.name = scene.getName();
        this.position = scene.position;
        this.playables = new ArrayList<>();
        playUnits= new ArrayList<>();
    }

    /**
     * Parse json string into a  scene.
     *
     * @param response the json string
     * @return the scene object
     */
    public static Scene parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(response, Scene.class);

    }

    /**
     * Gets scene dir name.
     *
     * @return the scene dir name
     */
    public String getSceneDirName() {
        return sceneDirName;
    }

    /**
     * Sets scene dir name.
     *
     * @param sceneDirName the scene dir name
     */
    public void setSceneDirName(String sceneDirName) {
        this.sceneDirName = sceneDirName;
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
     * Gets the position in the play.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position in the play.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the character color.
     *
     * @return the character color
     */
    public String getCharColor() {
        return charColor;
    }

    /**
     * Sets character color.
     *
     * @param charColor the character color
     */
    public void setCharColor(String charColor) {
        this.charColor = charColor;
    }

    /**
     * Gets character name.
     *
     * @return the character name
     */
    public String getCharName() {
        return charName;
    }

    /**
     * Sets character name.
     *
     * @param charName the character name
     */
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
        if(last == 0){
            Log.i("JUANLEONHEX2", "INSIDE SINGLE MACRO SCENE");
            return playUnits.get(last).play(msgCreators, senders, net, bundle);
        }
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


    /**
     * Gets play units.
     *
     * @return the play units
     */
    public List<PlayUnit> getPlayUnits() {
        return playUnits;
    }

    /**
     * Sets play units.
     *
     * @param playUnits the play units
     */
    public void setPlayUnits(List<PlayUnit> playUnits) {
        this.playUnits = playUnits;
    }
}
