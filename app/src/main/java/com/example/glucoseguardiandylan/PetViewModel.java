package com.example.glucoseguardiandylan;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private PetRepository repository;
    private LiveData<List<Pet>> allPets;
    private LiveData<Pet> getPet;
    private Pet petOG;


    public PetViewModel(@NonNull Application application) {
        super(application);
        repository = new PetRepository(application);
        allPets = repository.getAllPets();
        getPet = repository.getPet();
        petOG = repository.getPetOG();

    }

    public void insert(Pet pet){
        repository.insert(pet);
    }

    public void update(Pet pet){
        repository.update(pet);
    }

    public void delete(Pet pet){
        repository.delete(pet);
    }

    public void deleteAllPets(){
        repository.deleteAllPets();
    }

    public LiveData<List<Pet>> getAllPets(){
        return allPets;
    }

    public LiveData<Pet> getPet(){
        return getPet;
    }


    public int calculateHealth(double bloodSugar){

        int petHealth = repository.getPetOG().getHealth();
        Pet userPet = repository.getPetOG();

        if(bloodSugar <= 10 && bloodSugar >= 4) {
            if (petHealth >= 90) {
                petHealth = 100;
            } else {
                petHealth = petHealth + 10;
            }
        } else if(bloodSugar > 10 || bloodSugar < 4){
            petHealth = petHealth - 10;
            if(petHealth <= 0){
                petHealth = 5;
            }
        }

        userPet.setHealth(petHealth);
        repository.update(userPet);
        return petHealth;
    }

    public int calculateHunger(Long lastFeeding){
        int petHunger = getPetOG().getHunger();
        Pet userPet = repository.getPetOG();

        Long currentDateMilsec = System.currentTimeMillis();
        Long differenceDateMil = currentDateMilsec - lastFeeding;
        Long differenceDateMin = differenceDateMil/60000L;

        while (differenceDateMin > 30){
            petHunger = petHunger - 10;
            differenceDateMin =- 1L;
        }

        userPet.setHunger(petHunger);
        update(userPet);
        return petHunger;
    }


    public Pet getPetOG(){
        return repository.getPetOG();
    }

}
