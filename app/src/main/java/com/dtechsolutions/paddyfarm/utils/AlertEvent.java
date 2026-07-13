package com.dtechsolutions.paddyfarm.utils;

import android.view.View;

public class AlertEvent {
    public enum Type { SUCCESS, ERROR, CONFIRMATION, CUSTOM };

    private final Type type;
    private final String title;
    private final String message;
    private final View view;
    private boolean hasBeenHandled = false;

    public AlertEvent(Type type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.view = null;
    }

    public AlertEvent(Type type, View view) {
        this.type = type;
        this.view = view;
        this.title = "";
        this.message= "";
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

    public View getView() { return view; }

    public boolean getContentIfNotHandled() {
        if(hasBeenHandled) {
            return false;
        }else{
            hasBeenHandled = true;
            return true;
        }
    }
}
