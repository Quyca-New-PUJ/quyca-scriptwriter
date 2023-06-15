package com.quyca.scriptwriter.config;

public enum FixedConfiguredAction {
    emotions(false),
    forward(true),
    reverse(true),
    right(true),
    left(true),
    calibration(true),
    offset_der(false),
    offset_izq(false)
    ;

    private boolean answerable;

    FixedConfiguredAction(boolean answerable) {
        this.answerable = answerable;
    }

    public boolean isAnswerable() {
        return answerable;
    }

    public boolean isFixedAction(String s) {

        for (FixedConfiguredAction value : FixedConfiguredAction.values()) {
            if (s.equalsIgnoreCase(value.name())) {
                return true;
            }
        }
        return false;
    }
}
