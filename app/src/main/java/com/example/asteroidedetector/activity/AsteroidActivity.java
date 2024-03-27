package com.example.asteroidedetector.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asteroidedetector.R;
import com.example.asteroidedetector.adapter.AsteroidArrayAdapter;
import com.example.asteroidedetector.service.AsteroidService;
import com.example.asteroidedetector.views.SolarSystemView;

public class AsteroidActivity extends AppCompatActivity {
    private TextView name;
    private TextView magnitude;
    private TextView distance;
    private TextView orbitalPeriod;
    private SolarSystemView solarSystemView;
    private ImageView likeButton;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asteroid);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int id = getIntent().getIntExtra("id",0);
        this.name = findViewById(R.id.asteroidName);
        this.magnitude = findViewById(R.id.asteroidMagnitude);
        this.distance = findViewById(R.id.asteroidDistance);
        this.orbitalPeriod = findViewById(R.id.periodeOrbitale);
        this.solarSystemView = findViewById(R.id.solarSystemView);
        this.likeButton = findViewById(R.id.likeButton);
        this.settings = getSharedPreferences("asteroid", MODE_PRIVATE);
        AsteroidService asteroidService = AsteroidService.getInstance(getApplicationContext());
        asteroidService.getAsteroid(id)
                .then(response -> {
                    name.setText(response.getName());
                    magnitude.setText(getString(R.string.magnitude,response.getMagnitude()));
                    distance.setText(getString(R.string.distance,response.getDistance()));
                    orbitalPeriod.setText(getString(R.string.periode_orbitale,response.getOrbitalPeriod()));
                    solarSystemView.setAsteroid(response);
                    likeButton.setImageResource(settings.getBoolean(String.valueOf(id),false)  ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
                })
                .error(error -> Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations !",Toast.LENGTH_SHORT).show());
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX()) {
                    onFlingToLeft();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void onClickLikeButton(View view) {
        int id = getIntent().getIntExtra("id", 0);
        SharedPreferences.Editor editor = settings.edit();
        if (settings.getBoolean(String.valueOf(id), false)) {
            editor.putBoolean(String.valueOf(id), false);
            likeButton.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            editor.putBoolean(String.valueOf(id), true);
            likeButton.setImageResource(android.R.drawable.btn_star_big_on);
        }
        editor.apply();
    }

    public void onFlingToLeft() {
        finish();
    }
}