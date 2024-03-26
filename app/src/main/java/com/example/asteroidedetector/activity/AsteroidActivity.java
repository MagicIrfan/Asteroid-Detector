package com.example.asteroidedetector.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asteroidedetector.R;
import com.example.asteroidedetector.adapter.AsteroidArrayAdapter;
import com.example.asteroidedetector.service.AsteroidService;

public class AsteroidActivity extends AppCompatActivity {
    private TextView name;
    private TextView magnitude;
    private TextView distance;
    private TextView orbitalPeriod;
    private AsteroidService asteroidService;

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
        this.name = (TextView) findViewById(R.id.asteroidName);
        this.magnitude = (TextView) findViewById(R.id.asteroidMagnitude);
        this.distance = (TextView) findViewById(R.id.asteroidDistance);
        this.orbitalPeriod = (TextView) findViewById(R.id.periodeOrbitale);
        asteroidService = AsteroidService.getInstance(getApplicationContext());
        asteroidService.getAsteroid(id)
                .then(response -> {
                    name.setText(response.getName());
                    magnitude.setText(getString(R.string.magnitude,response.getMagnitude()));
                    distance.setText(getString(R.string.distance,response.getDistance()));
                    orbitalPeriod.setText(getString(R.string.periode_orbitale,response.getOrbitalPeriod()));
                })
                .error(error -> Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations !",Toast.LENGTH_SHORT).show());
    }
}