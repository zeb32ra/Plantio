package com.example.plantio;

import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PlantDictionary {


    public Map<String, String> map = new HashMap<String, String>();


    public PlantDictionary() {
    }

    public void load_data(){
        map.put("Георгин", "images/georgin.jpg");
        map.put("Ива", "images/iva.jpg");

        map.put("Плохо", "images/sick_icon.png");
        map.put("Нормально", "images/neutral_icon.png");
        map.put("Отлично", "images/best_icon.png");

        map.put("Полив", "images/watering_can.png");
        map.put("Опрыскивание", "images/spray.png");
        map.put("Удобрение", "images/fertilizer.png");
        map.put("Обрезка", "images/pruning.png");

        map.put("Отличное состояние", "images/best_icon.png");
        map.put("Плохое состояние", "images/sick_icon.png");
        map.put("Нормальное состояние", "images/neutral_icon.png");

        map.put("2", "Понедельник");
        map.put("3", "Вторник");
        map.put("4", "Среда");
        map.put("5", "Четверг");
        map.put("6", "Пятница");
        map.put("7", "Суббота");
        map.put("1", "Воскресенье");

    }


}
