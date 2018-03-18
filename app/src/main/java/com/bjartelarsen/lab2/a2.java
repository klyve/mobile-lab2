package com.bjartelarsen.lab2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

public class a2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2);
        getSupportActionBar().setTitle("Settings");


        NumberPicker limit = findViewById(R.id.frequency);
        NumberPicker frequency = findViewById(R.id.limit);

        limit.setMinValue(1);
        limit.setMaxValue(20);
        limit.setWrapSelectorWheel(false);

        frequency.setMinValue(1);
        frequency.setMaxValue(10);
        frequency.setWrapSelectorWheel(false);

        addEventListenerToSaveButton();
        getSharedPreferences();
    }

    protected void addEventListenerToSaveButton() {
        Button button = findViewById(R.id.savebtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), serviceWorker.class));
                saveSharedPreferences();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveSharedPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("FileName",0);

        NumberPicker limit = findViewById(R.id.limit);
        NumberPicker frequency = findViewById(R.id.frequency);
        EditText rssUrl = findViewById(R.id.rssuri);

        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("limit", limit.getValue());
        prefEditor.putInt("frequency", frequency.getValue());
        prefEditor.putString("uri", rssUrl.getText().toString());
        prefEditor.apply();
    }


    private void getSharedPreferences() {

        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);

        int limitNum = sharedPref.getInt("limit",-1);
        int freq = sharedPref.getInt("frequency", -1);
        String url = sharedPref.getString("uri", "");


        NumberPicker limit = findViewById(R.id.limit);
        NumberPicker frequency = findViewById(R.id.frequency);
        EditText rssUrl = findViewById(R.id.rssuri);

        limit.setValue(limitNum);
        frequency.setValue(freq);
        rssUrl.setText(url);
    }
}
