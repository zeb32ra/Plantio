package com.example.plantio;

public class Plant {
private String name;

private String watering_amount;
private String sun_amount;
private String danger;

    public Plant(String name, String watering_amount,
                 String sun_amount, String danger, String all_about_watering, String all_about_sun,
                 String all_about_spraying, String all_about_fertilizing, String all_about_earth,
                 String all_about_transfer, String all_about_danger, String all_about_cutting) {
        this.name = name;
        this.watering_amount = watering_amount;
        this.sun_amount = sun_amount;
        this.danger = danger;
        this.all_about_watering = all_about_watering;
        this.all_about_sun = all_about_sun;
        this.all_about_spraying = all_about_spraying;
        this.all_about_fertilizing = all_about_fertilizing;
        this.all_about_earth = all_about_earth;
        this.all_about_transfer = all_about_transfer;
        this.all_about_danger = all_about_danger;
        this.all_about_cutting = all_about_cutting;
    }
    private String all_about_watering;
private String all_about_sun;
private String all_about_spraying;
private String all_about_fertilizing;
private String all_about_earth;
private String all_about_transfer;
private String all_about_danger;

private String all_about_cutting;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWatering_amount() {
        return watering_amount;
    }

    public void setWatering_amount(String watering_amount) {
        this.watering_amount = watering_amount;
    }

    public String getSun_amount() {
        return sun_amount;
    }

    public void setSun_amount(String sun_amount) {
        this.sun_amount = sun_amount;
    }

    public String getDanger() {
        return danger;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getAll_about_watering() {
        return all_about_watering;
    }

    public void setAll_about_watering(String all_about_watering) {
        this.all_about_watering = all_about_watering;
    }

    public String getAll_about_sun() {
        return all_about_sun;
    }

    public void setAll_about_sun(String all_about_sun) {
        this.all_about_sun = all_about_sun;
    }

    public String getAll_about_spraying() {
        return all_about_spraying;
    }

    public void setAll_about_spraying(String all_about_spraying) {
        this.all_about_spraying = all_about_spraying;
    }

    public String getAll_about_fertilizing() {
        return all_about_fertilizing;
    }

    public void setAll_about_fertilizing(String all_about_fertilizing) {
        this.all_about_fertilizing = all_about_fertilizing;
    }

    public String getAll_about_earth() {
        return all_about_earth;
    }

    public void setAll_about_earth(String all_about_earth) {
        this.all_about_earth = all_about_earth;
    }

    public String getAll_about_transfer() {
        return all_about_transfer;
    }

    public void setAll_about_transfer(String all_about_transfer) {
        this.all_about_transfer = all_about_transfer;
    }

    public String getAll_about_danger() {
        return all_about_danger;
    }

    public void setAll_about_danger(String all_about_danger) {
        this.all_about_danger = all_about_danger;
    }

    public String getAll_about_cutting() {
        return all_about_cutting;
    }

    public void setAll_about_cutting(String all_about_cutting) {
        this.all_about_cutting = all_about_cutting;
    }

}
