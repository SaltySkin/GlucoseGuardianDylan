package com.example.glucoseguardiandylan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "feeding_table") //room, used to hide boilerplate code
public class Feeding {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double bloodSugar;
    private String description;
    private int carbs;
    private Long date;

    public Feeding(double bloodSugar, String description, int carbs) {
        this.bloodSugar = bloodSugar;
        this.description = description;
        this.carbs = carbs;
        this.date = Calendar.getInstance().getTimeInMillis();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public String getDescription() {
        return description;
    }

    public int getCarbs() {
        return carbs;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
