package com.bjartelarsen.lab2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class a3 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            final String titleText = bundle.getString("title");
            getSupportActionBar().setTitle(titleText);

            TextView description = findViewById(R.id.description);
            description.setText(bundle.getString("description"));

            String link = bundle.getString("link");
            addEventListenerOnButton(link);
        }


    }

    public void addEventListenerOnButton(final String uri) {
        Button button = findViewById(R.id.link);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(browserIntent);
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
