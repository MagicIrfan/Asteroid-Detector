package com.example.asteroidedetector.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.example.asteroidedetector.service.AsteroidService;

public class AsteroidListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private TextView textView;

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
        AsteroidService asteroidService = AsteroidService.getInstance(getApplicationContext());
        this.listView = findViewById(R.id.listView);
        this.textView = findViewById(R.id.asteroidText);
        asteroidService.getAsteroids()
                .then(response -> {
                    Toast.makeText(getApplicationContext(), "Données reçues !",Toast.LENGTH_SHORT).show();
                    AsteroidArrayAdapter adapter = new AsteroidArrayAdapter(getApplicationContext(),response);
                    listView.setAdapter(adapter);
                    textView.setText(getString(R.string.nombre_asteroides, response.size()));
                })
                .error(error -> Toast.makeText(getApplicationContext(), "Erreur lors de la récupération des informations !",Toast.LENGTH_SHORT).show());
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), AsteroidActivity.class);
        intent.putExtra("id",view.getId());
        startActivity(intent);
    }
}