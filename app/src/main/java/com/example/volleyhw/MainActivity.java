package com.example.volleyhw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button requestButton = (Button) findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequest();
            }
        });
    }

    private void makeRequest() {
        // Instantiate the RequestQueue.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                // Show an explanation to the user
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
            }
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://date.jsontest.com/";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String date, mse, time = "";
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                date = jsonResponse.getString("date");
                                mse = jsonResponse.getString("milliseconds_since_epoch");
                                time = jsonResponse.getString("time");
                            } catch (JSONException e) {
                                date = "Unable to parse date";
                                mse = "Unable to parse mse";
                                time = "Unable to parse time";
                            }
                            setTableCellValues(date, mse, time);
                        }
                    }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                setTableCellValues("Unable to fetch date",
                                        "Unable to fetch mse",
                                        "Unable to fetch time");
                        }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    private void setTableCellValues(String date, String mse, String time) {
        TextView dateTV = (TextView)findViewById(R.id.dateValueTV);
        TextView mseTV = (TextView)findViewById(R.id.mseValueTV);
        TextView timeTV = (TextView)findViewById(R.id.timeValueTV);
        dateTV.setText(date);
        mseTV.setText(mse);
        timeTV.setText(time);
    }
}