package com.example.asteroidedetector.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asteroidedetector.R;
import com.example.asteroidedetector.adapter.AsteroidArrayAdapter;
import com.example.asteroidedetector.model.AsteroidModel;
import com.example.asteroidedetector.service.AsteroidService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AsteroidListActivity extends AppCompatActivity {

    private ListView listView;
    private TextView textView;
    private AsteroidService asteroidService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asteroid_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.example.asteroidedetector.R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.asteroidService = AsteroidService.getInstance(getApplicationContext());
        this.listView = (ListView) findViewById(R.id.listView);
        this.textView = (TextView) findViewById(R.id.asteroidText);
        asteroidService.getAsteroids()
                .then(response -> {
                    Toast.makeText(getApplicationContext(), "Données reçues !",Toast.LENGTH_SHORT).show();
                    AsteroidArrayAdapter adapter = new AsteroidArrayAdapter(getApplicationContext(),response);
                    listView.setAdapter(adapter);
                    textView.setText(getString(R.string.nombre_asteroides, response.size()));
                })
                .error(error -> Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations !",Toast.LENGTH_SHORT).show());
    }
}