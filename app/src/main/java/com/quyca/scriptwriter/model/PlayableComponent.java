package com.quyca.scriptwriter.model;

import android.util.Log;

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
import java.util.List;
import java.util.Map;

/**
 * The type Playable component. It represents a component that can be played and that is composed by multiple playables.
 */
public abstract class PlayableComponent extends Playable implements Serializable {
    /**
     * The Playables that compose this component.
     */
    @Expose
    protected List<Playable> playables;

    @Override
    public NetBundle play(Map<String, QuycaMessageTransformer> msgCreators, Map<String, RobotExecutioner> senders, PetriNet net, UIBundle bundle) {
        TransitionBuilder tBuilder = net.getCreator().createNewTransition();
        ArcBuilder aBuilder = net.getCreator().createNewArc();
        NetBundle netBundle = new NetBundle();

        Transition toPlace = null, fromPlace;
        int last = playables.size() - 1;

        NetBundle actBundle;
        List<Place> top;
        List<Place> bottom;
        for (int i = 0; i <= last; i++) {
            actBundle = playables.get(i).play(msgCreators, senders, net, bundle);
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

    @Override
    public QuycaCommandState getDone() {
        QuycaCommandState state = QuycaCommandState.TO_EXECUTE;
        state = playables.stream().allMatch(p->p.getDone().equals(QuycaCommandState.DONE))?QuycaCommandState.DONE:state;
        state = playables.stream().anyMatch(p->p.getDone().equals(QuycaCommandState.IN_EXECUTION))?QuycaCommandState.IN_EXECUTION:state;
        state = playables.stream().anyMatch(p->p.getDone().equals(QuycaCommandState.ERROR))?QuycaCommandState.ERROR:state;
        return state;
    }

    @Override
    public void setDone(QuycaCommandState done) {
        playables.forEach(playable -> playable.setDone(done));
    }

    /**
     * Gets playables.
     *
     * @return the playables
     */
    public List<Playable> getPlayables() {
        return playables;
    }

    /**
     * Sets playables.
     *
     * @param playables the playables
     */
    public void setPlayables(List<Playable> playables) {
        this.playables = playables;
    }

}
