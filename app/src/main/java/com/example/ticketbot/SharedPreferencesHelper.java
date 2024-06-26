package com.example.ticketbot;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "ticket_prefs";
    private static final String TICKETS_KEY = "tickets";
    private static final String SEATS_KEY = "seats";

    public static void saveTickets(Context context, List<Ticket> tickets) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tickets);
        editor.putString(TICKETS_KEY, json);
        editor.apply();
    }

    public static List<Ticket> loadTickets(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(TICKETS_KEY, null);
        Type type = new TypeToken<List<Ticket>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveUnavailableSeats(Context context, String play, String time, Set<String> seats) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Map<String, Set<String>> unavailableSeatsMap = loadUnavailableSeatsMap(context);
        String key = play + " at " + time;
        unavailableSeatsMap.put(key, seats);
        String json = gson.toJson(unavailableSeatsMap);
        editor.putString(SEATS_KEY, json);
        editor.apply();
    }

    public static Set<String> loadUnavailableSeats(Context context, String play, String time) {
        Map<String, Set<String>> unavailableSeatsMap = loadUnavailableSeatsMap(context);
        String key = play + " at " + time;
        return unavailableSeatsMap.getOrDefault(key, new HashSet<>());
    }

    private static Map<String, Set<String>> loadUnavailableSeatsMap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SEATS_KEY, null);
        Type type = new TypeToken<Map<String, Set<String>>>() {}.getType();
        Map<String, Set<String>> unavailableSeatsMap = gson.fromJson(json, type);
        if (unavailableSeatsMap == null) {
            unavailableSeatsMap = new HashMap<>();
        }
        return unavailableSeatsMap;
    }

    public static void initializeDefaultUnavailableSeats(Context context) {
        // Initialize default unavailable seats for plays if not already set
        Map<String, Set<String>> unavailableSeatsMap = loadUnavailableSeatsMap(context);

        addDefaultSeats(unavailableSeatsMap, "Hamlet", "19:00", getDefaultHamletSeats());
        addDefaultSeats(unavailableSeatsMap, "Hamlet", "22:00", getDefaultHamletSeats());
        addDefaultSeats(unavailableSeatsMap, "Romeo and Juliet", "19:00", getDefaultRomeoSeats());
        addDefaultSeats(unavailableSeatsMap, "Romeo and Juliet", "22:00", getDefaultRomeoSeats());

        saveUnavailableSeatsMap(context, unavailableSeatsMap);
    }

    private static void addDefaultSeats(Map<String, Set<String>> map, String play, String time, Set<String> seats) {
        String key = play + " at " + time;
        if (!map.containsKey(key)) {
            map.put(key, seats);
        }
    }

    private static Set<String> getDefaultHamletSeats() {
        Set<String> seats = new HashSet<>();
        seats.add("Row 0 Seat 1");
        seats.add("Row 0 Seat 2");
        seats.add("Row 0 Seat 3");
        seats.add("Row 1 Seat 4");
        seats.add("Row 1 Seat 5");
        seats.add("Row 1 Seat 6");
        seats.add("Row 2 Seat 7");
        seats.add("Row 2 Seat 8");
        seats.add("Row 2 Seat 9");
        seats.add("Row 7 Seat 10");
        return seats;
    }

    private static Set<String> getDefaultRomeoSeats() {
        Set<String> seats = new HashSet<>();
        seats.add("Row 7 Seat 10");
        seats.add("Row 7 Seat 9");
        seats.add("Row 7 Seat 8");
        seats.add("Row 6 Seat 7");
        seats.add("Row 6 Seat 6");
        seats.add("Row 6 Seat 5");
        seats.add("Row 5 Seat 4");
        seats.add("Row 5 Seat 3");
        seats.add("Row 5 Seat 2");
        seats.add("Row 0 Seat 1");
        return seats;
    }

    private static void saveUnavailableSeatsMap(Context context, Map<String, Set<String>> unavailableSeatsMap) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(unavailableSeatsMap);
        editor.putString(SEATS_KEY, json);
        editor.apply();
    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
