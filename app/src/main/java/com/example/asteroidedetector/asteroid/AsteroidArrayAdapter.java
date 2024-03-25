package com.example.asteroidedetector.asteroid;

import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.asteroidedetector.R;

import java.util.List;

public class AsteroidArrayAdapter extends ArrayAdapter<AsteroidModel> {
    private final Context context;
    private final List<AsteroidModel> asteroidsData;

    public AsteroidArrayAdapter(Context context, List<AsteroidModel> asteroidsData) {
        super(context,0,asteroidsData);
        this.context = context;
        this.asteroidsData = asteroidsData;
    }

    @Override
    public int getCount() {
        return asteroidsData.size();
    }

    @Override
    public AsteroidModel getItem(int position) {
        return asteroidsData.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);
        }

        TextView asteroidName = convertView.findViewById(R.id.asteroidName);
        TextView asteroidMagnitude = convertView.findViewById(R.id.asteroidMagnitude);
        TextView asteroidDistance = convertView.findViewById(R.id.asteroidDistance);

        AsteroidModel asteroid = asteroidsData.get(position);
        asteroidName.setText(asteroid.getName());
        asteroidMagnitude.setText(context.getString(R.string.magnitude, asteroid.getMagnitude()));
        asteroidDistance.setText(context.getString(R.string.distance, asteroid.getDistance()));

        return convertView;
    }
}
