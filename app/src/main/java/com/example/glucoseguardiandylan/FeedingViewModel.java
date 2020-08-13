package com.example.glucoseguardiandylan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FeedingViewModel extends AndroidViewModel {
    private FeedingRepository repository;
    private LiveData<List<Feeding>> allFeedings;

    public FeedingViewModel(@NonNull Application application) {
        super(application);
        repository = new FeedingRepository(application);
        allFeedings = repository.getAllFeedings();
    }

    public void insert(Feeding feeding){
        repository.insert(feeding);
    }

    public void update(Feeding feeding){
        repository.update(feeding);
    }

    public void delete(Feeding feeding){
        repository.delete(feeding);
    }

    public void deleteAllFeedings(){
        repository.deleteAllFeedings();
    }

    public Feeding getLatestFeeding(){
        return repository.getLatestFeeding();
    }

    public LiveData<List<Feeding>> getAllFeedings(){
        return allFeedings;
    }
}
