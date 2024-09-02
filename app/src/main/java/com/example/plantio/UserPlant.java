package com.example.plantio;

public class UserPlant {
    private String last_cutting_date;
    private String last_fertilizing_date;
    private String last_spraying_date;
    private String last_watering_date;
    private String plant_name;
    private String plant_type;
    private String user;

    public String getLast_cutting_date() {
        return last_cutting_date;
    }

    public void setLast_cutting_date(String last_cutting_date) {
        this.last_cutting_date = last_cutting_date;
    }

    public String getLast_fertilizing_date() {
        return last_fertilizing_date;
    }

    public void setLast_fertilizing_date(String last_fertilizing_date) {
        this.last_fertilizing_date = last_fertilizing_date;
    }

    public String getLast_spraying_date() {
        return last_spraying_date;
    }

    public void setLast_spraying_date(String last_spraying_date) {
        this.last_spraying_date = last_spraying_date;
    }

    public String getLast_watering_date() {
        return last_watering_date;
    }

    public void setLast_watering_date(String last_watering_date) {
        this.last_watering_date = last_watering_date;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getPlant_type() {
        return plant_type;
    }

    public void setPlant_type(String plant_type) {
        this.plant_type = plant_type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UserPlant(String last_cutting_date, String last_fertilizing_date, String last_spraying_date, String last_watering_date, String plant_name, String plant_type, String user) {
        this.last_cutting_date = last_cutting_date;
        this.last_fertilizing_date = last_fertilizing_date;
        this.last_spraying_date = last_spraying_date;
        this.last_watering_date = last_watering_date;
        this.plant_name = plant_name;
        this.plant_type = plant_type;
        this.user = user;
    }
}
