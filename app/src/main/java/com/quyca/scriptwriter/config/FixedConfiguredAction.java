package com.quyca.scriptwriter.config;

public enum FixedConfiguredAction {
    CAMBIAR_PANTALLA, forward,reverse, right, left;
    public boolean isFixedAction(String s){

        for (FixedConfiguredAction value : FixedConfiguredAction.values()) {
            if(s.equalsIgnoreCase(value.name())){
                return true;
            }
        }
        return false;
    }
}
