package com.example.asteroidedetector.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class SolarSystemView extends View {

    private Paint paintSun, paintEarth, paintOrbitSun, paintOrbitEarth, paintMoon;

    public SolarSystemView(Context context) {
        super(context);
        init(null, 0);
    }

    public SolarSystemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SolarSystemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Initialiser le Paint pour le Soleil
        paintSun = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSun.setStyle(Paint.Style.FILL);
        paintSun.setColor(Color.YELLOW); // Soleil jaune

        // Initialiser le Paint pour la Terre
        paintEarth = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintEarth.setStyle(Paint.Style.FILL);
        paintEarth.setColor(Color.BLUE); // Terre bleue

        // Initialiser le Paint pour l'orbite du Soleil
        paintOrbitSun = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOrbitSun.setStyle(Paint.Style.STROKE);
        paintOrbitSun.setStrokeWidth(2f); // Largeur de l'orbite du Soleil
        paintOrbitSun.setColor(Color.LTGRAY); // Orbite du Soleil grise claire

        // Initialiser le Paint pour l'orbite de la Terre
        paintOrbitEarth = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOrbitEarth.setStyle(Paint.Style.STROKE);
        paintOrbitEarth.setStrokeWidth(2f); // Largeur de l'orbite de la Terre
        paintOrbitEarth.setColor(Color.DKGRAY); // Orbite de la Terre grise foncée

        // Initialiser le Paint pour la Lune
        paintMoon = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMoon.setStyle(Paint.Style.FILL);
        paintMoon.setColor(Color.GRAY); // Lune grise
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Dimensions de la vue
        int width = getWidth();
        int height = getHeight();

        // Dessiner le Soleil au centre
        float sunRadius = height / 15f; // Taille du Soleil
        canvas.drawCircle(width / 2f, height / 2f, sunRadius, paintSun);

        // Dessiner l'orbite de la Terre
        float orbitEarthRadius = height / 3f; // Rayon de l'orbite de la Terre plus grand pour être visible
        canvas.drawCircle(width / 2f, height / 2f, orbitEarthRadius, paintOrbitEarth);

        // Dessiner la Terre sur son orbite autour du Soleil
        float earthRadius = height / 25f; // Taille de la Terre
        float earthOrbitDegree = 45; // Position actuelle de la Terre sur son orbite
        float earthX = (float) (width / 2 + orbitEarthRadius * Math.cos(Math.toRadians(earthOrbitDegree)));
        float earthY = (float) (height / 2 + orbitEarthRadius * Math.sin(Math.toRadians(earthOrbitDegree)));
        canvas.drawCircle(earthX, earthY, earthRadius, paintEarth);

        // Dessiner l'orbite de la Lune autour de la Terre
        float orbitMoonRadius = earthRadius * 2; // La Lune est plus proche de la Terre dans notre modèle
        canvas.drawCircle(earthX, earthY, orbitMoonRadius, paintOrbitEarth); // Utiliser paintOrbitEarth pour la simplicité

        // Dessiner la Lune sur son orbite autour de la Terre
        float moonRadius = earthRadius / 3; // Taille de la Lune par rapport à celle de la Terre
        float moonOrbitDegree = 0; // Position actuelle de la Lune sur son orbite
        float moonX = (float) (earthX + orbitMoonRadius * Math.cos(Math.toRadians(moonOrbitDegree))); // Choisir un angle pour positionner la Lune
        float moonY = (float) (earthY + orbitMoonRadius * Math.sin(Math.toRadians(moonOrbitDegree)));
        canvas.drawCircle(moonX, moonY, moonRadius, paintMoon);
    }
}


