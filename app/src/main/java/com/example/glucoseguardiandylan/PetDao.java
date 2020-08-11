package com.example.glucoseguardiandylan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao //One DAO per Entity
public interface PetDao {
    @Insert
    void insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);

    @Query("DELETE FROM pet_table") //allows you to make your own queries
    void deleteAllPets();

    @Query("SELECT * FROM pet_table")
    LiveData<List<Pet>> getAllPets();

    @Query("SELECT * FROM pet_table WHERE id = :id")
    LiveData<Pet> getPet(int id);
}
