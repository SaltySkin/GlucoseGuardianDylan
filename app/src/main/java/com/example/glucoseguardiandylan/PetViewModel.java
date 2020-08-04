package com.example.glucoseguardiandylan;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private PetRepository repository;
    private LiveData<List<Pet>> allPets;

    public PetViewModel(@NonNull Application application) {
        super(application);
        repository = new PetRepository(application);
        allPets = repository.getAllPets();
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
}
