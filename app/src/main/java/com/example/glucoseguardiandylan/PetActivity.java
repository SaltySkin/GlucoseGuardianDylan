package com.example.glucoseguardiandylan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

public class PetActivity extends AppCompatActivity {
    private FeedingViewModel feedingViewModel;
    private PetViewModel petViewModel;

    //Used in startActivityForResult class to id requests
    public static final int ADD_FEEDING_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        feedingViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FeedingViewModel.class);
        petViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PetViewModel.class);
        updateHealthBar(petViewModel.getPetOG().getHealth());
        setTitle("Pet View");


        //updateHungerBar();
        //hungerBar.setProgress(petHunger);

        //String formattedDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(currentFeeding.getDate());
        //feedingViewModel.getLatestDate();

        Button buttonAddFeeding = findViewById(R.id.button_add_feeding_pet);
        buttonAddFeeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetActivity.this, AddEditFeedingActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button buttonGotoLogbook = findViewById(R.id.view_logbook_button);
        buttonGotoLogbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logbookIntent = new Intent(PetActivity.this, MainActivity.class);
                startActivity(logbookIntent);
            }
        });

//        final Observer<Pet> nameObserver = new Observer<Pet>() {
//            @Override
//            public void onChanged(@Nullable final Pet updatedPet) {
//                // Update the UI, in this case, a Pet.
//                petViewModel.update(updatedPet);
//            }
//        };



    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHungerBar();
        Toast.makeText(this, "Hunger bar updated", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FEEDING_REQUEST && resultCode == RESULT_OK) {

            double bloodSugar = Objects.requireNonNull(data).getDoubleExtra(AddEditFeedingActivity.EXTRA_BLOOD_SUGAR, 0);
            String description = data.getStringExtra(AddEditFeedingActivity.EXTRA_DESCRIPTION);
            int carbs = data.getIntExtra(AddEditFeedingActivity.EXTRA_CARBS, 0);

            Feeding feeding = new Feeding(bloodSugar, description, carbs);
            feedingViewModel.insert(feeding);

            Toast.makeText(this, "Feeding saved", Toast.LENGTH_SHORT).show();

            int petHealth = petViewModel.calculateHealth(bloodSugar);

            if (petHealth <= 5){
                Toast.makeText(this, "You lost too much health, your pet passed out and health in now 5, take care of you blood sugar keep him healthy",  Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Take care of your Pet, & your blood sugar to keep him healthy",  Toast.LENGTH_LONG).show();
            }

              updateHealthBar(petHealth);
//            Pet pet = petViewModel.getPetOG();
//            pet.setHunger(pet.getHunger() + 30);
//            petViewModel.update(pet);


        } else {
            Toast.makeText(this, "Feeding not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateHealthBar(int health) {
        ProgressBar healthBar = findViewById(R.id.health_bar);
        healthBar.setProgress(health, true);
    }

    public void updateHungerBar() {
        ProgressBar hungerBar = findViewById(R.id.hunger_bar);
        int petHunger = petViewModel.calculateHunger();
        hungerBar.setProgress(petHunger, true);

    }

//    public void calculateHealth(double bloodSugar){
//        LiveData<List<Pet>> petsLiveData = petViewModel.getAllPets();
//        List<Pet> pets = new ArrayList<>();
//        pets =  petsLiveData.getValue();
//        Pet userPet = null;
//        if (pets != null) {
//            userPet = pets.get(0);
//        } else{
//            petViewModel.insert(new Pet("Vampy 2", 50, 50));
//            petsLiveData = petViewModel.getAllPets();
//            pets =  petsLiveData.getValue();
//            userPet = pets.get(0);
//        }
//        int petHealth = userPet.getHealth();
//        if(bloodSugar <= 10 || bloodSugar >= 4) {
//            if (petHealth >= 90) {
//                petHealth = 100;
//            } else {
//                petHealth =+ 10;
//            }
//        } else if(bloodSugar > 10 || bloodSugar < 4){
//            petHealth =- 10;
//            if (petHealth <= 0){
//                Toast.makeText(this, "You pet has no health, GAME OVER", Toast.LENGTH_SHORT).show();
//                petHealth = 25;
//                Toast.makeText(this, "Pet revived, health 25", Toast.LENGTH_LONG).show();
//            }
//            else{
//                Toast.makeText(this, "Blood sugar out of range, Pet has taken damage", Toast.LENGTH_LONG).show();
//            }
//        }
//        petViewModel.update(userPet);
//    }
}