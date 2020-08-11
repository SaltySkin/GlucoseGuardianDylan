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

    public PetViewModel(@NonNull Application application) {
        super(application);
        repository = new PetRepository(application);
        allPets = repository.getAllPets();
        getPet = repository.getPet();
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
        return getPet();
    }
}
