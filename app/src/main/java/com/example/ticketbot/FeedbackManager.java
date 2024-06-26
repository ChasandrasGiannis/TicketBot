package com.example.ticketbot;

import android.content.Context;
import android.content.SharedPreferences;

public class FeedbackManager {
    private static final String PREFS_NAME = "FeedbackPrefs";
    private static final String KEY_FEEDBACKS = "feedbacks";
    private SharedPreferences sharedPreferences;

    public FeedbackManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveFeedback(String feedback) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String existingFeedbacks = sharedPreferences.getString(KEY_FEEDBACKS, "");
        existingFeedbacks += feedback + "\n";
        editor.putString(KEY_FEEDBACKS, existingFeedbacks);
        editor.apply();
    }

    public String getAllFeedbacks() {
        return sharedPreferences.getString(KEY_FEEDBACKS, "No feedbacks yet");
    }
}




