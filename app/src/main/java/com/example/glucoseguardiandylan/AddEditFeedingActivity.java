package com.example.glucoseguardiandylan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditFeedingActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.glucoseguardiandylan.EXTRA_ID";
    public static final String EXTRA_BLOOD_SUGAR = "com.example.glucoseguardiandylan.EXTRA_BLOOD_SUGAR";
    public static final String EXTRA_DESCRIPTION = "com.example.glucoseguardiandylan.EXTRA_DESCRIPTION";
    public static final String EXTRA_CARBS = "com.example.glucoseguardiandylan.EXTRA_CARBS";

    private EditText editTextBloodSugar;
    private EditText editTextDescription;
    private NumberPicker numberPickerCarbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feeding);
        editTextBloodSugar = findViewById(R.id.edit_text_blood_sugar);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerCarbs = findViewById(R.id.number_picker_carbs);
        numberPickerCarbs.setMinValue(0);
        numberPickerCarbs.setMaxValue(250);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);


        // Check if the feeding already exists, if it does title is shown as edit feeding, if not it is add feeding
        Intent intent = getIntent(); //Gets intent that started this activity & contains all the data
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Feeding");
            //primitive int isn't allowed to be null, default value must also be passed
            editTextBloodSugar.setText(String.valueOf(intent.getDoubleExtra(EXTRA_BLOOD_SUGAR,0)));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerCarbs.setValue(intent.getIntExtra(EXTRA_CARBS, 0));
        } else {
            setTitle("Add Feeding");
        }
    }

    private void saveFeeding() {
        String bloodSugarString = editTextBloodSugar.getText().toString();
        //check if the field is empty, done as a string before being converted to a double
        if(bloodSugarString.trim().isEmpty()) {
            Toast.makeText(this, "Please enter your blood sugar", Toast.LENGTH_SHORT).show();
            return;
        }

        //converts blood sugar string to double to be stored in database
        double bloodSugar = Double.parseDouble(bloodSugarString);
        String description = editTextDescription.getText().toString();
        int carbs = numberPickerCarbs.getValue();

        Intent feedingIntent = new Intent();
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            feedingIntent.putExtra(EXTRA_ID, id);
        }
        feedingIntent.putExtra(EXTRA_BLOOD_SUGAR, bloodSugar);
        feedingIntent.putExtra(EXTRA_DESCRIPTION, description);
        feedingIntent.putExtra(EXTRA_CARBS, carbs);


        setResult(RESULT_OK, feedingIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_feeding_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_feeding:
                saveFeeding();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
