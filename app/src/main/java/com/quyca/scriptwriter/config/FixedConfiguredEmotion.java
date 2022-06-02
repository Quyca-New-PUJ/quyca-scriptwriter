package com.quyca.scriptwriter.config;

public enum FixedConfiguredEmotion {
    veryhappy, happy, sad, verysad, neutral, angry, sick, surprised;

    public boolean isFixedEmotion(String s){
        for (FixedConfiguredEmotion value : FixedConfiguredEmotion.values()) {
            if(s.equalsIgnoreCase(value.name())){
                return true;
            }
        }
        return false;
    }
}
