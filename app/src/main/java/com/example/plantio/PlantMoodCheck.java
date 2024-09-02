package com.example.plantio;

public class PlantMoodCheck {
    private String date;
    private String mood;
    private String plant_id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public PlantMoodCheck(String date, String mood, String plant_id) {
        this.date = date;
        this.mood = mood;
        this.plant_id = plant_id;
    }
}
