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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private TextView petName;
    private TextView petLevel;

    //Used in startActivityForResult class to id requests
    public static final int ADD_FEEDING_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
//        ImageView imgHigh = findViewById(R.id.petImageHigh);
//        ImageView imgMed = findViewById(R.id.petImageLow);

        feedingViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FeedingViewModel.class);
        petViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PetViewModel.class);
        petName = findViewById(R.id.petName);
        petName.setText(petViewModel.getPetOG().getName());
        updateHealthBar(petViewModel.getPetOG().getHealth());
        setImageVisiblity();
        updateExperienceBar();
        setTitle("Your Pet");


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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_FEEDING_REQUEST && resultCode == RESULT_OK) {

            double bloodSugar = Objects.requireNonNull(data).getDoubleExtra(AddEditFeedingActivity.EXTRA_BLOOD_SUGAR, 0);
            String foodInfo = data.getStringExtra(AddEditFeedingActivity.EXTRA_FOOD_INFO);
            int carbs = data.getIntExtra(AddEditFeedingActivity.EXTRA_CARBS, 0);
            String mealInfo = data.getStringExtra(AddEditFeedingActivity.EXTRA_MEAL_INFO);

            Feeding feeding = new Feeding(bloodSugar, foodInfo, carbs, mealInfo);
            feedingViewModel.insert(feeding);

            int petHealth = petViewModel.calculateHealth(bloodSugar);

            if (petHealth == 0){
                Toast.makeText(this, "You lost too much health, your pet went down a level, feed him to increase his health",  Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Take care of your Pet, & your blood sugar to keep him healthy",  Toast.LENGTH_LONG).show();
            }

            setImageVisiblity();

            updateHealthBar(petHealth);
            Pet pet = petViewModel.getPetOG();
            pet.setHunger(pet.getHunger() + 30);
            petViewModel.update(pet);

            int expIncrease = petViewModel.calculateExperienceLevel(bloodSugar);
            updateExperienceBar();

            Toast.makeText(this, "Your Pet has went up " + expIncrease + " Experience Points", Toast.LENGTH_LONG).show();



        } else {
            Toast.makeText(this, "Feeding not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void setImageVisiblity(){
        ImageView imgHigh = findViewById(R.id.petImageHigh);
        ImageView imgMed = findViewById(R.id.petImageLow);

        if(petViewModel.isHigh()){
            imgHigh.setVisibility(View.VISIBLE);
            imgMed.setVisibility(View.INVISIBLE);
        } else if(petViewModel.isLow()){
            imgMed.setImageResource(R.drawable.med_vampire);
            imgMed.setVisibility(View.VISIBLE);
            imgHigh.setVisibility(View.INVISIBLE);
        }
    }
    public void updateHealthBar(int health) {
        ProgressBar healthBar = findViewById(R.id.health_bar);
        healthBar.setProgress(health, true);
    }

    public void updateHungerBar() {
        ProgressBar hungerBar = findViewById(R.id.hunger_bar);
        ProgressBar healthBar = findViewById(R.id.health_bar);


        int petHunger = petViewModel.calculateHunger();
        int petHealth = petViewModel.getPetHealth();

        healthBar.setProgress(petHealth);
        hungerBar.setProgress(petHunger);

    }

    public void updateExperienceBar(){
        ProgressBar expBar = findViewById(R.id.experience_bar);
        petLevel = findViewById(R.id.pet_level);
        int experiencePoints = petViewModel.getPetOG().getExperiencePoints();
        int level = petViewModel.getPetOG().getLevel();
        petLevel.setText(String.valueOf(level));
        expBar.setProgress(experiencePoints);
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