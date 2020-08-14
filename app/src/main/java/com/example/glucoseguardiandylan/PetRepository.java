package com.example.glucoseguardiandylan;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PetRepository {
    private PetDao petDao;
    private LiveData<List<Pet>> allPets;
    private LiveData<Pet> getPet;
    private Pet pet;
    ExecutorService executorService;

    public PetRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        petDao = database.petDao();
        allPets = petDao.getAllPets();
        //getPet = petDao.getPet(0);
        pet = petDao.getPetOG(1);
    }
        //Database Operation Methods (API that repository shows to the outside)
    public void insert(Pet pet) {
        AppDatabase.databaseExecutor.execute(new InsertExecutor(petDao, pet));
    }

    public void update(Pet pet){
        AppDatabase.databaseExecutor.execute(new UpdateExecutor(petDao, pet));
    }

    public void delete(Pet pet){
        AppDatabase.databaseExecutor.execute(new DeleteExecutor(petDao, pet));
    }

    public void deleteAllPets(){
        AppDatabase.databaseExecutor.execute(new DeleteAllPetsExecutor(petDao));
    }

    public LiveData<List<Pet>> getAllPets() {

        return allPets;
    }

    public Pet getPetOG(){
        return petDao.getPetOG(1);
    }

    public LiveData<Pet> getPet() {
        return getPet;
    }



    private static class InsertExecutor implements Runnable{

        private PetDao petDao;
        private Pet pet;

        public InsertExecutor(PetDao petDao, Pet pet) {
            super();
            this.petDao = petDao;
            this.pet = pet;
        }

        @Override
        public void run() {
            petDao.insert(pet);
        }
    }

    private static class UpdateExecutor implements Runnable{

        private PetDao petDao;
        private Pet pet;

        public UpdateExecutor(PetDao petDao, Pet pet) {
            super();
            this.petDao = petDao;
            this.pet = pet;
        }

        @Override
        public void run() {
            petDao.update(pet);
        }
    }

    private static class DeleteExecutor implements Runnable{

        private PetDao petDao;
        private Pet pet;

        public DeleteExecutor(PetDao petDao, Pet pet) {
            super();
            this.petDao = petDao;
            this.pet = pet;
        }

        @Override
        public void run() {
            petDao.delete(pet);
        }
    }

    private static class DeleteAllPetsExecutor implements Runnable{

        private PetDao petDao;

        public DeleteAllPetsExecutor(PetDao petDao) {
            super();
            this.petDao = petDao;
        }

        @Override
        public void run() {
            petDao.deleteAllPets();
        }

    }
}
