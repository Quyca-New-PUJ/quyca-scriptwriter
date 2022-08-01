package com.quyca.scriptwriter.integ.backend.model;

import java.util.List;

public class ActionList {

    private String name;
    private List<ActionDTO> actions;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ActionDTO> getActions() {
        return actions;
    }
    public void setActions(List<ActionDTO> actions) {
        this.actions = actions;
    }
    @Override
    public String toString() {
        return "STActionListInDTO [name=" + name + ", actions=" + actions.size() + "]";
    }

}
