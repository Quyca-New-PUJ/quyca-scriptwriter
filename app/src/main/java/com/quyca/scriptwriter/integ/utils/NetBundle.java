package com.quyca.scriptwriter.integ.utils;

import com.quyca.robotmanager.net.Place;

import java.util.ArrayList;
import java.util.List;

public class NetBundle {
    List<Place> topPlaces;
    List<Place> bottomPlaces;

    public NetBundle() {
        this.topPlaces = new ArrayList<>();
        this.bottomPlaces = new ArrayList<>();
    }

    public NetBundle(List<Place> topPlaces, List<Place> bottomPlaces) {
        this.topPlaces = topPlaces;
        this.bottomPlaces = bottomPlaces;
    }

    public List<Place> getTopPlaces() {
        return topPlaces;
    }

    public void setTopPlaces(List<Place> topPlaces) {
        this.topPlaces = topPlaces;
    }

    public List<Place> getBottomPlaces() {
        return bottomPlaces;
    }

    public void setBottomPlaces(List<Place> bottomPlaces) {
        this.bottomPlaces = bottomPlaces;
    }
}
