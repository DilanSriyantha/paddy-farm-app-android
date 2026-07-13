package com.dtechsolutions.paddyfarm.data.models;

import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;

public class LanguageItem {
    private PreferredLanguage preferredLanguage;
    private String caption;

    public LanguageItem(PreferredLanguage preferredLanguage, String caption) {
        this.preferredLanguage = preferredLanguage;
        this.caption = caption;
    }

    public PreferredLanguage getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(PreferredLanguage preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return caption;
    }
}
