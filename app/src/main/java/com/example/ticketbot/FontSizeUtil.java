package com.example.ticketbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

public class FontSizeUtil {
    private static final String PREFS_NAME = "FontSizePrefs";
    private static final String FONT_SIZE_KEY = "fontSize";
    private static final float DEFAULT_FONT_SIZE = 16; // Default font size

    public static void saveFontSize(Context context, float fontSize) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(FONT_SIZE_KEY, fontSize);
        editor.apply();
    }

    public static float loadFontSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(FONT_SIZE_KEY, DEFAULT_FONT_SIZE);
    }

    public static void applyFontSize(Context context, TextView textView) {
        float fontSize = loadFontSize(context);
        textView.setTextSize(fontSize);
    }

    public static void applyFontSize(Context context, TextView... textViews) {
        float fontSize = loadFontSize(context);
        for (TextView textView : textViews) {
            textView.setTextSize(fontSize);
        }
    }
}

