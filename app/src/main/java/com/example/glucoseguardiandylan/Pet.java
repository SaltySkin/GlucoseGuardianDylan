package com.example.glucoseguardiandylan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "pet_table")
public class Pet {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int health;
    private int hunger;

    public Pet(String name, int health, int hunger) {
        this.name = name;
        this.health = health;
        this.hunger = hunger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

}