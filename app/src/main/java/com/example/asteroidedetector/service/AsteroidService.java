package com.example.asteroidedetector.service;

import android.content.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.asteroidedetector.model.AsteroidDetailModel;
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
        String today = getTodaysDate();
        String apiUrl = "https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s";
        apiUrl = String.format(apiUrl,today,today,API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response ->  {
                    if (promise.then != null) {
                        try {
                            List<AsteroidModel> asteroids = getListFromJSONObject(response);
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

    public Promise<AsteroidDetailModel> getAsteroid(int id){
        Promise<AsteroidDetailModel> promise = new Promise<>();
        String apiUrl = "https://api.nasa.gov/neo/rest/v1/neo/%s?api_key=%s";
        apiUrl = String.format(apiUrl,id,API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                response ->  {
                    if (promise.then != null) {
                        try {
                            promise.then.execute(getAsteroidDetailFromJSONObject(response));
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

    private List<AsteroidModel> getListFromJSONObject(JSONObject response) throws JSONException {
        List<AsteroidModel> asteroidsData = new ArrayList<>();
        JSONObject jsonObject = response.getJSONObject("near_earth_objects");
        jsonObject.keys().forEachRemaining(key -> {
            try{
                JSONArray asteroids = jsonObject.getJSONArray(key);
                for (int index = 0; index < asteroids.length(); index++) {
                    asteroidsData.add(getAsteroidFromJSONObject(asteroids.getJSONObject(index)));
                }
            }
            catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        return asteroidsData;
    }

    private AsteroidModel getAsteroidFromJSONObject(JSONObject response) throws JSONException {
        JSONObject missDistance = getTodaysMissDistance(response);
        return new AsteroidModel(response.getString("id"), response.getString("name"), response.getDouble("absolute_magnitude_h"), missDistance != null ? missDistance.getInt("kilometers") : 0);
    }

    private AsteroidDetailModel getAsteroidDetailFromJSONObject(JSONObject response) throws JSONException {
        AsteroidModel asteroid = getAsteroidFromJSONObject(response);
        int orbitalPeriod = response.getJSONObject("orbital_data").getInt("orbital_period");
        return new AsteroidDetailModel(
                asteroid.getId(),
                asteroid.getName(),
                asteroid.getMagnitude(),
                asteroid.getDistance(),
                orbitalPeriod
                );
    }

    private JSONObject getTodaysMissDistance(JSONObject response) throws JSONException {
        JSONArray closeApproachData = response.getJSONArray("close_approach_data");
        String today = getTodaysDate();

        for (int i = 0; i < closeApproachData.length(); i++) {
            JSONObject approachData = closeApproachData.getJSONObject(i);
            if (isTodaysDate(approachData, today)) {
                return approachData.getJSONObject("miss_distance");
            }
        }
        return null;
    }

    private String getTodaysDate() {
        return new SimpleDateFormat("yyyy-MM-dd",Locale.US).format(new Date());
    }

    private boolean isTodaysDate(JSONObject approachData, String today) throws JSONException {
        return approachData.getString("close_approach_date").equals(today);
    }
}
