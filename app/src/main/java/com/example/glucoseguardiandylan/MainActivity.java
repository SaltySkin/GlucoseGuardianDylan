package com.example.glucoseguardiandylan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FeedingViewModel feedingViewModel;
    //Used in startActivityForResult class to id requests
    public static final int ADD_FEEDING_REQUEST = 1;
    public static final int EDIT_FEEDING_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); // Doe this if you know the views size wont change, more efficient

        final FeedingRecyclerAdapter adapter = new FeedingRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton buttonAddFeeding = findViewById(R.id.button_add_feeding);
        buttonAddFeeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditFeedingActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button buttonViewPet = findViewById(R.id.button_view_pet);
        buttonViewPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PetActivity.class);
                startActivity(intent);
            }
        });

        //Implements the method for editing feedings
        adapter.setOnItemClickListener(new FeedingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Feeding feeding) {
                Intent intent = new Intent(MainActivity.this, AddEditFeedingActivity.class);
                //Sending the data to the database
                intent.putExtra(AddEditFeedingActivity.EXTRA_ID, feeding.getId());
                intent.putExtra(AddEditFeedingActivity.EXTRA_BLOOD_SUGAR, feeding.getBloodSugar());
                intent.putExtra(AddEditFeedingActivity.EXTRA_DESCRIPTION, feeding.getDescription());
                intent.putExtra(AddEditFeedingActivity.EXTRA_CARBS, feeding.getCarbs());
                startActivityForResult(intent, EDIT_FEEDING_REQUEST); //Pass the intent and the request code
            }
        });

        //tells the ViewModel which lifecycle it has to scope to
        feedingViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(FeedingViewModel.class);
        feedingViewModel.getAllFeedings().observe(this, new Observer<List<Feeding>>() {
            @Override
            public void onChanged(List<Feeding> feedings) { //Every time onChanged is called, recycler view adapter is updated and refreshed
                adapter.setFeedings(feedings);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                feedingViewModel.delete(adapter.getFeedingAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Feeding deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_FEEDING_REQUEST && resultCode == RESULT_OK){
            double bloodSugar = Objects.requireNonNull(data).getDoubleExtra(AddEditFeedingActivity.EXTRA_BLOOD_SUGAR, 0);
            String description = data.getStringExtra(AddEditFeedingActivity.EXTRA_DESCRIPTION);
            int carbs = data.getIntExtra(AddEditFeedingActivity.EXTRA_CARBS, 0);

            Feeding feeding = new Feeding(bloodSugar, description, carbs);
            feedingViewModel.insert(feeding);

            Toast.makeText(this, "Feeding saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_FEEDING_REQUEST && resultCode == RESULT_OK){
            //default value set to -1 so that if an item is invalid, it will caught as the id auto generator never sets to -1
            int id = data.getIntExtra(AddEditFeedingActivity.EXTRA_ID, -1);

            if(id == -1){ //Checks for invalid ID
                Toast.makeText(this, "Feeding cant be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            double bloodSugar = data.getDoubleExtra(AddEditFeedingActivity.EXTRA_BLOOD_SUGAR, 0);
            String description = data.getStringExtra(AddEditFeedingActivity.EXTRA_DESCRIPTION);
            int carbs = data.getIntExtra(AddEditFeedingActivity.EXTRA_CARBS, 1);
            Feeding feeding = new Feeding(bloodSugar, description, carbs);
            feeding.setId(id);
            feedingViewModel.update(feeding);
            Toast.makeText(this, "Feeding Edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Feeding not saved", Toast.LENGTH_SHORT).show();
        }
    }

    //menu option for delete all
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_feedings:
                feedingViewModel.deleteAllFeedings();
                Toast.makeText(this, "All feedings deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}