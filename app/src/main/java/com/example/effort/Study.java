package com.example.effort;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Study extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
    }

    public void showAlamDialog(View view){
        TimePickerFragment timePickerFragment=new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(),"timePicker");
    }

}
