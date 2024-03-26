package com.example.asteroidedetector.service;

import android.content.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asteroidedetector.model.AsteroidModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AsteroidService {
    private static AsteroidService instance;
    private static final String API_KEY = "e1GkZAJUw7hBH1YakSEVcpICtmpZ8SYe7YyelilN";
    private final RequestQueue queue;
    private final Context context;

    private AsteroidService(Context context){
        this.queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public static synchronized AsteroidService getInstance(Context context){
        if(instance == null){
            instance = new AsteroidService(context);
        }
        return instance;
    }

    public static synchronized AsteroidService getInstance(){
        if(instance == null){
            throw new RuntimeException("CONTEXT_NOT_INITIALIZED");
        }
        return instance;
    }

    public Promise<List<AsteroidModel>> getAsteroids() {
        Promise<List<AsteroidModel>> promise = new Promise<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = dateFormat.format(new Date());
        String apiUrl = "https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s";
        apiUrl = String.format(apiUrl,today,today,API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response ->  {
                    if (promise.then != null) {
                        try {
                            List<AsteroidModel> asteroids = fromJSONObject(response);
                            promise.then.execute(asteroids);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error ->  {
                    if (promise.error != null) {
                        promise.error.execute(error.getMessage());
                    }
                }
        );
        queue.add(request);
        return promise;
    }

    private List<AsteroidModel> fromJSONObject(JSONObject response) throws JSONException {
        List<AsteroidModel> asteroidsData = new ArrayList<>();
        JSONObject jsonObject = response.getJSONObject("near_earth_objects");
        jsonObject.keys().forEachRemaining(key -> {
            try{
                JSONArray asteroids = jsonObject.getJSONArray(key);
                for (int index = 0; index < asteroids.length(); index++) {
                    JSONObject asteroid = asteroids.getJSONObject(index);
                    JSONArray approachData = asteroid.getJSONArray("close_approach_data");
                    JSONObject missDistance = approachData.getJSONObject(0).getJSONObject("miss_distance");
                    asteroidsData.add(new AsteroidModel(asteroid.getString("name"), asteroid.getDouble("absolute_magnitude_h"), missDistance.getInt("kilometers")));
                }
            }
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return asteroidsData;
    }
}
