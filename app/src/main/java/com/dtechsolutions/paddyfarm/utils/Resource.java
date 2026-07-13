package com.dtechsolutions.paddyfarm.utils;

public class Resource <T>{
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    public Status status;
    public T data;
    public String message;

    private boolean hasBeenHandled = false;

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(
                Status.SUCCESS,
                data,
                null
        );
    }

    public static <T> Resource<T> error(String message) {
        return new Resource<>(
                Status.ERROR,
                null,
                message
        );
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(
                Status.LOADING,
                null,
                null
        );
    }

    public T getContentIfNotHandled() {
        if(hasBeenHandled) {
            return null;
        }else{
            hasBeenHandled = true;
            return data;
        }
    }

    public boolean isHandled() {
        return hasBeenHandled;
    }

    public void makeHandled() {
        this.hasBeenHandled = true;
    }
}
