package com.quyca.scriptwriter.config;

public enum FixedConfiguredEmotion {
    MUY_FELIZ,FELIZ, TRISTE, MUY_TRISTE, NEUTRO,FURIOSO,ENFERMO,SORPRENDIDO;

    public boolean isFixedEmotion(String s){
        for (FixedConfiguredEmotion value : FixedConfiguredEmotion.values()) {
            if(s.equalsIgnoreCase(value.name())){
                return true;
            }
        }
        return false;
    }
}
