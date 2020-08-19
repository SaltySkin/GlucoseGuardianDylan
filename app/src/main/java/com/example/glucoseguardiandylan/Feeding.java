package com.example.glucoseguardiandylan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Calendar;

//Feeding table, feedings are the information about what
@Entity(tableName = "feeding_table") //room notation, less boilerplate code needed
public class Feeding {
    //Variables
    @PrimaryKey(autoGenerate = true) // Makes int id the primary key and incrementally auto generates a new id with each new feeding object
    private int id;
    private double bloodSugar; //stores blood sugar level
    private String foodInfo; //food that/if eaten during this blood sugar check
    private int carbs; //amount of carbs eaten this blood sugar check
    private String mealInfo; //whether the user as eaten a meal or is about to in relation to this feeding
    private Long date; //date/time at the time of recorded reading

    //Constructor
    public Feeding(double bloodSugar, String foodInfo, int carbs, String mealInfo) {
        this.bloodSugar = bloodSugar;
        this.foodInfo = foodInfo;
        this.carbs = carbs;
        this.mealInfo =mealInfo;
        this.date = Calendar.getInstance().getTimeInMillis(); //gets time/date at time on object initialisation
    }

    //getters and setters
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getBloodSugar() {
        return bloodSugar;
    }

    public String getFoodInfo() {
        return foodInfo;
    }

    public int getCarbs() {
        return carbs;
    }

    public String getMealInfo() {
        return mealInfo;
    }

    public void setMealInfo(String mealInfo) {
        this.mealInfo = mealInfo;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
