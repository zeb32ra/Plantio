package com.example.plantio;

public class Notification {
    private String plant_id;
    private String name;
    private String week_day;
    private String time;

    public Notification(String plant_id, String name, String week_day, String time) {
        this.plant_id = plant_id;
        this.name = name;
        this.week_day = week_day;
        this.time = time;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
