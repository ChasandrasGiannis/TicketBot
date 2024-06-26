package com.example.ticketbot;

import java.util.Map;

public class Play {

    private String name;
    private String description;
    private String cast;
    private String[] timetable;
    private String auditorium;
    private String duration;

    public Play(String name, String description, String cast, String auditorium, String duration) {
        this.name = name;
        this.description = description;
        this.cast = cast;
        this.auditorium = auditorium;
        this.duration = duration;
    }

    public Play(String name, String auditorium) {
        this.name = name;
        this.auditorium = auditorium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String[] getTimetable() {
        return timetable;
    }

    public void setTimetable(String[] timetable) {
        this.timetable = timetable;
    }

    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
