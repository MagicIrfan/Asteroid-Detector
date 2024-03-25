package com.example.asteroidedetector;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ListView listView = (ListView) findViewById(R.id.listView);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=e1GkZAJUw7hBH1YakSEVcpICtmpZ8SYe7YyelilN";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Données reçues !",Toast.LENGTH_SHORT).show();
                        List<String> asteroidName = new ArrayList<>();
                        try {
                            JSONObject jsonObject = (JSONObject) response.get("near_earth_objects");
                            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                                String key = it.next();
                                JSONArray asteroids = (JSONArray) jsonObject.get(key);
                                for (int i = 0; i < asteroids.length(); i++) {
                                    JSONObject asteroid = asteroids.getJSONObject(i); // Obtenez chaque objet du tableau comme un JSONObject
                                    asteroidName.add((String) asteroid.get("name"));
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout,R.id.textview, asteroidName);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gérez les erreurs ici
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }
}