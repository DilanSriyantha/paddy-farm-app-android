package com.dtechsolutions.paddyfarm.utils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import com.dtechsolutions.paddyfarm.enums.PreferredLanguage;

public class LanguageManager {
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SINHALA = "si";

    public static void setAppLanguage(String languageCode) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageCode);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

    public static void setAppLanguage(PreferredLanguage preferredLanguage) {
        String languageCode = preferredLanguage == PreferredLanguage.ENGLISH ? LANGUAGE_ENGLISH : LANGUAGE_SINHALA;
        setAppLanguage(languageCode);
    }

    public static String getCurrentLanguage() {
        LocaleListCompat currentLocales = AppCompatDelegate.getApplicationLocales();
        if(!currentLocales.isEmpty()) {
            return currentLocales.get(0).getLanguage();
        }
        return LANGUAGE_ENGLISH;
    }

    public static PreferredLanguage getCurrentPreferredLanguage() {
        String currentLanguage = getCurrentLanguage();
        if(currentLanguage.equals(LANGUAGE_ENGLISH)) {
            return PreferredLanguage.ENGLISH;
        }
        return PreferredLanguage.SINHALA;
    }
}
