package com.quyca.scriptwriter.integ.utils;

import com.quyca.robotmanager.net.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Net bundle. It gathers all the related objects surrounding a playable object after being converted to a place..
 */
public class NetBundle {
    /**
     * The Top places, that represent the entry point of the sequence made by a playable.
     */
    List<Place> topPlaces;
    /**
     * The Bottom places , that represent the exit point of the sequence made by a playable.
     */
    List<Place> bottomPlaces;

    /**
     * Instantiates a new Net bundle.
     */
    public NetBundle() {
        this.topPlaces = new ArrayList<>();
        this.bottomPlaces = new ArrayList<>();
    }

    /**
     * Instantiates a new Net bundle.
     *
     * @param topPlaces    the top places
     * @param bottomPlaces the bottom places
     */
    public NetBundle(List<Place> topPlaces, List<Place> bottomPlaces) {
        this.topPlaces = topPlaces;
        this.bottomPlaces = bottomPlaces;
    }

    /**
     * Gets top places.
     *
     * @return the top places
     */
    public List<Place> getTopPlaces() {
        return topPlaces;
    }

    /**
     * Sets top places.
     *
     * @param topPlaces the top places
     */
    public void setTopPlaces(List<Place> topPlaces) {
        this.topPlaces = topPlaces;
    }

    /**
     * Gets bottom places.
     *
     * @return the bottom places
     */
    public List<Place> getBottomPlaces() {
        return bottomPlaces;
    }

    /**
     * Sets bottom places.
     *
     * @param bottomPlaces the bottom places
     */
    public void setBottomPlaces(List<Place> bottomPlaces) {
        this.bottomPlaces = bottomPlaces;
    }
}
