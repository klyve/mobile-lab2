package com.bjartelarsen.lab2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.webkit.URLUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class serviceWorker extends Service {


    private RequestQueue    queue       = null;
    int                     limit   = 0;
    int                     frequency   = 0;
    String                  rssuri;


    public serviceWorker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        // Get the intial user preferences
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        getUserPreference();
        getRSSFeed();

        final Handler handler = new Handler();
        // Runs repeatably every given minute
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Calls other function that runs the service
                getUserPreference();
                getRSSFeed();
                handler.postDelayed(this, frequency * 60000); // delay on frequency times a minute
            }
        }, 0);
    }

    public void getUserPreference() {
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        frequency = sharedPref.getInt("frequency", -1);
        limit = sharedPref.getInt("limit",-1);
        rssuri = sharedPref.getString("uri", "");

    }

    private void saveToSharedPreferences(String xml) {
        SharedPreferences sharedPref = getSharedPreferences("FileName",0);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("data", xml);

        prefEditor.apply();
    }


    public void getRSSFeed() {
        if(!URLUtil.isValidUrl(rssuri)) return;
        StringRequest req = new StringRequest(Request.Method.GET, rssuri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                saveToSharedPreferences(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(req);
    }
}
