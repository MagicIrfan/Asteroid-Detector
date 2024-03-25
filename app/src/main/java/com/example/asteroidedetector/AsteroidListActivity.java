package com.example.asteroidedetector;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.example.asteroidedetector.adapters.AsteroidArrayAdapter;
import com.example.asteroidedetector.model.AsteroidModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsteroidListActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asteroid_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.asteroidText);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = getJsonObjectRequest();
        queue.add(jsonObjectRequest);
    }

    @NonNull
    private JsonObjectRequest getJsonObjectRequest() {
        String url = "https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=e1GkZAJUw7hBH1YakSEVcpICtmpZ8SYe7YyelilN";
        return new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        handleResponse(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations !",Toast.LENGTH_SHORT).show()
        );
    }

    private void handleResponse(JSONObject response) throws JSONException{
        Toast.makeText(getApplicationContext(), "Données reçues !",Toast.LENGTH_SHORT).show();
        List<AsteroidModel> asteroidsData = new ArrayList<>();
        JSONObject jsonObject = (JSONObject) response.get("near_earth_objects");
        jsonObject.keys().forEachRemaining(key -> {
            try {
                JSONArray asteroids = jsonObject.getJSONArray(key);
                for (int index = 0; index < asteroids.length(); index++) {
                    JSONObject asteroid = asteroids.getJSONObject(index);
                    JSONArray approachData = asteroid.getJSONArray("close_approach_data");
                    JSONObject missDistance = approachData.getJSONObject(0).getJSONObject("miss_distance");
                    asteroidsData.add(new AsteroidModel(asteroid.getString("name"), asteroid.getDouble("absolute_magnitude_h"), missDistance.getDouble("kilometers")));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        AsteroidArrayAdapter adapter = new AsteroidArrayAdapter(getApplicationContext(),asteroidsData);
        listView.setAdapter(adapter);
        textView.setText(getString(R.string.nombre_asteroides, response.getInt("element_count")));
    }
}