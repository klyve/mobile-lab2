package com.bjartelarsen.lab2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.ArrayAdapter;
import java.util.ArrayList;

import android.widget.ListView;




import javax.xml.XMLConstants;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class MainActivity extends AppCompatActivity {


    String                  xml;
    private JSONObject feed        = null;
    int                     limit   = 0;
    JSONArray itemArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a1);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("RSS Feed");

        addEventListenerToPreferenceButton();

        if (!isServiceRunning()) {
            Intent serviceIntent = new Intent(getApplicationContext(), serviceWorker.class);
            this.startService(serviceIntent);
        }



        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUserPreference();
                                updateListView();
                            }
                        });
                    }
                }, 500);

    }

    private void getUserPreference() {
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        limit = sharedPref.getInt("limit",-1);
        xml = sharedPref.getString("data", "");
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        feed = xmlToJson.toJson();
    }

    private void updateListView() {
        ListView list = findViewById(R.id.listView);

        ArrayList<String> stringArr = new ArrayList<>();

        try {
            itemArray = feed.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

            for (int i = 0; i < limit; i++) {
                String title = itemArray.getJSONObject(i).getString("title");
                stringArr.add(title);
            }
        } catch (JSONException e) {
            Log.e("JSON exception", e.getMessage());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringArr);
        list.setAdapter(adapter);

        addEventListenerToListView();

    }

    protected void addEventListenerToListView() {
        ListView listview = findViewById(R.id.listView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), a3.class);
                try {
                    intent.putExtra("title", itemArray.getJSONObject(position).getString("title"));
                    intent.putExtra("description", itemArray.getJSONObject(position).getString("description"));
                    intent.putExtra("link", itemArray.getJSONObject(position).getString("link"));
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                }
                startActivity(intent);
            }
        });
    }

    protected void addEventListenerToPreferenceButton() {
        Button button = findViewById(R.id.preferencesButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), a2.class);
                startActivity(intent);
            }
        });
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceWorker.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
