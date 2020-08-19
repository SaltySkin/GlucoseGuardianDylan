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

    //constructor, passes in application gets pet data through repo
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

    //gets live data pet
    public LiveData<Pet> getPet(){
        return getPet;
    }

    //used to calculate pet health after feeding is entered
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

                if(userPet.getLevel() > 0){
                    userPet.setLevel(userPet.getLevel() - 1);
                }

                petHealth = 0;

            }
        }



        userPet.setHealth(petHealth);
        repository.update(userPet);
        return petHealth;
    }

    public int calculateHunger(){
        int petHunger = getPetOG().getHunger();
        Pet userPet = repository.getPetOG();
        Long lastAppAccess = userPet.getLastAccessedApp();
        Long currentDateMilsec = System.currentTimeMillis();

        Long differenceDateMil = currentDateMilsec - lastAppAccess;
        Long differenceDateMin = differenceDateMil/60000L;

        while (differenceDateMin >= 1){
            petHunger = petHunger - 10;
            differenceDateMin =- 1L;
        }

        if(petHunger <= 0){
            userPet.setHealth(userPet.getHealth() - 10);
            petHunger = 1;
        }

        userPet.setHunger(petHunger);
        userPet.setLastAccessedApp(System.currentTimeMillis());
        update(userPet);
        return petHunger;
    }

    public int calculateExperienceLevel(double bloodSugar){
        int exp = getPetOG().getExperiencePoints();
        int lvl = getPetOG().getLevel();
        int expIncrease = 0;

        //gives experience depending on the blood sugar entered
        if(bloodSugar >=  4 && bloodSugar <= 8.5){
            expIncrease = 5;
            exp = exp + expIncrease;
        } else if(bloodSugar > 8.5 && bloodSugar < 10){
            expIncrease = 2;
            exp = exp + expIncrease;
        }

        //carried over left over experience after the level goes uo
        if(exp >= 100){
            lvl = lvl + 1;
            exp = exp%100;
        }

        Pet pet = getPetOG();
        pet.setLevel(lvl);
        pet.setExperiencePoints(exp);
        update(pet);
        return expIncrease;
    }

    public boolean isHigh(){
        return getPetOG().getHealth() >= 50;
    }

    public boolean isLow(){
        return getPetOG().getHealth() < 50;
    }

    public Pet getPetOG(){
        return repository.getPetOG();
    }

    public int getPetHealth(){
        return getPetOG().getHealth();
    }

}
