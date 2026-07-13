package com.dtechsolutions.paddyfarm.data.models;

import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;

public class UserUpdateRequest {
    private String name;
    private String phoneNumber;
    private PreferredLanguage preferredLanguage;

    public UserUpdateRequest(String name, String phoneNumber, PreferredLanguage preferredLanguage) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.preferredLanguage = preferredLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PreferredLanguage getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(PreferredLanguage preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
