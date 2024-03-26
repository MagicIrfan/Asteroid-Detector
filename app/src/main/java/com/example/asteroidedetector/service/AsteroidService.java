package com.example.asteroidedetector.service;

import android.content.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public void getAsteroids(Response.Listener<JSONObject> response, Response.ErrorListener error) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = dateFormat.format(new Date());
        String apiUrl = "https://api.nasa.gov/neo/rest/v1/feed?start_date=%s&end_date=%s&api_key=%s";
        apiUrl = String.format(apiUrl,today,today,API_KEY);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                /*new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        List<AsteroidModel> asteroids = new ArrayList<>();
                        responseList.onResponse(asteroids);
                    }
                },*/
                response,
                error
        );
        queue.add(request);
    }
}
