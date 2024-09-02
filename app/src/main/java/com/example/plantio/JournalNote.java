package com.example.plantio;

public class JournalNote {
    private String action;
    private String date;
    private String plant_id;
    private String user_commet;
    private String time;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(String plant_id) {
        this.plant_id = plant_id;
    }

    public String getUser_commet() {
        return user_commet;
    }

    public void setUser_commet(String user_commet) {
        this.user_commet = user_commet;
    }

    public JournalNote(String action, String date, String plant_id, String user_commet, String time) {
        this.action = action;
        this.date = date;
        this.plant_id = plant_id;
        this.user_commet = user_commet;
        this.time = time;
    }
}
