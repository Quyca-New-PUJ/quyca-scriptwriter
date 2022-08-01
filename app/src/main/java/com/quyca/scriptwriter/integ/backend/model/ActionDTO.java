package com.quyca.scriptwriter.integ.backend.model;

public class ActionDTO {

    private String action;
    private String emotion;
    private Float time;
    private Float speed;

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getEmotion() {
        return emotion;
    }
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public Float getTime() {
        return time;
    }
    public void setTime(Float time) {
        this.time = time;
    }
    public Float getSpeed() {
        return speed;
    }
    public void setSpeed(Float speed) {
        this.speed = speed;
    }
    @Override
    public String toString() {
        return "Action [action=" + action + ", emotion=" + emotion + ", time=" + time + ", speed=" + speed + "]";
    }
}
