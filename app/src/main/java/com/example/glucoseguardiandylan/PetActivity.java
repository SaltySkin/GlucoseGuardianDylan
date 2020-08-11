package com.example.glucoseguardiandylan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PetActivity extends AppCompatActivity {
    private FeedingViewModel feedingViewModel;
    private PetViewModel petViewModel;

    //Used in startActivityForResult class to id requests
    public static final int ADD_FEEDING_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        setTitle("Pet View");

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

        feedingViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FeedingViewModel.class);
        petViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PetViewModel.class);


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

            //feedingViewModel.calculateHealth(bloodSugar);

        } else {
            Toast.makeText(this, "Feeding not saved", Toast.LENGTH_SHORT).show();
        }
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