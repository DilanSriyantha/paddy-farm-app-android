package com.dtechsolutions.paddyfarm.utils;

public class AlertEvent {
    public enum Type { SUCCESS, ERROR, CONFIRMATION };

    private final Type type;
    private final String title;
    private final String message;
    private boolean hasBeenHandled = false;

    public AlertEvent(Type type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean getContentIfNotHandled() {
        if(hasBeenHandled) {
            return false;
        }else{
            hasBeenHandled = true;
            return true;
        }
    }
}
