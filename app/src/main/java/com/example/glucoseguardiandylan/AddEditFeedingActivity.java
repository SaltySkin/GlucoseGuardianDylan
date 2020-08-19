package com.example.glucoseguardiandylan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddEditFeedingActivity extends AppCompatActivity {

    //Intent extra content that is used to hold the user input and pass it back to the pet activity, to be processed in the on result method.
    public static final String EXTRA_ID = "com.example.glucoseguardiandylan.EXTRA_ID";
    public static final String EXTRA_BLOOD_SUGAR = "com.example.glucoseguardiandylan.EXTRA_BLOOD_SUGAR";
    public static final String EXTRA_FOOD_INFO = "com.example.glucoseguardiandylan.EXTRA_FOOD_INFO";
    public static final String EXTRA_CARBS = "com.example.glucoseguardiandylan.EXTRA_CARBS";
    public static final String EXTRA_MEAL_INFO = "com.example.glucoseguardiandylan.EXTRA_MEAL_INFO";




    private EditText editTextBloodSugar;
    private EditText editTextFoodInfo;
    private NumberPicker numberPickerCarbs;
    private RadioGroup radioGroup;
    private RadioButton radioBeforeMeal;
    private RadioButton radioAfterMeal;
    private RadioButton radioNoMeal;

    private String mealInfo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feeding);

        editTextBloodSugar = findViewById(R.id.edit_text_blood_sugar);
        editTextFoodInfo = findViewById(R.id.edit_text_food_info);

        numberPickerCarbs = findViewById(R.id.number_picker_carbs);
        numberPickerCarbs.setMinValue(0);
        numberPickerCarbs.setMaxValue(250);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group_meal);
        radioBeforeMeal = (RadioButton) findViewById(R.id.radio_before_meal);
        radioAfterMeal = (RadioButton) findViewById(R.id.radio_after_meal);
        radioNoMeal = (RadioButton) findViewById(R.id.radio_no_meal);
        radioGroup.clearCheck();

        //Displays icon instead of text when actionbar buttons are compressed (The save icon)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);


        // Check if the feeding already exists, if it does title is shown as edit feeding, if not it is add feeding
        Intent intent = getIntent(); //Gets intent that started this activity & contains all the data
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Feeding");
            //primitive int isn't allowed to be null, default value must also be passed
            editTextBloodSugar.setText(String.valueOf(intent.getDoubleExtra(EXTRA_BLOOD_SUGAR,0)));
            editTextFoodInfo.setText(intent.getStringExtra(EXTRA_FOOD_INFO));
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
        String foodInfo = editTextFoodInfo.getText().toString();
        int carbs = numberPickerCarbs.getValue();

        //creates new intent for the feeding information to pass it to the pet activity
        Intent feedingIntent = new Intent();
        //Gets the id from the intent, setting the default value as -1 as -1 will not be generated as an idea
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        //checks if the id already exists, as it would not be -1(the default value), if it adds the feedings id variable
        if(id != -1){
            feedingIntent.putExtra(EXTRA_ID, id);
        }

        //Adds the rest of the extras information to the intent object
        feedingIntent.putExtra(EXTRA_BLOOD_SUGAR, bloodSugar);
        feedingIntent.putExtra(EXTRA_FOOD_INFO, foodInfo);
        feedingIntent.putExtra(EXTRA_CARBS, carbs);
        feedingIntent.putExtra(EXTRA_MEAL_INFO, mealInfo);

        //Sets the result check as okay and passes the feeding parameter, then ends the activity;
        setResult(RESULT_OK, feedingIntent);
        finish();
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_before_meal:
                if (checked)
                    mealInfo = "Before Meal";
                    break;

            case R.id.radio_after_meal:
                if (checked)
                    mealInfo = "After Meal";
                    break;

            case R.id.radio_no_meal:
                if (checked)
                    mealInfo = "No Meal";
                    break;
        }
    }

    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    //Menu method that passes in a menu object inflates it to be used by the save feeding option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_feeding_menu, menu);
        return true;
    }

    //Puts the save feeding menu item icon in the menu, when clicked it will run the save feeding method.
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
