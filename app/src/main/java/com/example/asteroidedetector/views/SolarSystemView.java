package com.example.asteroidedetector.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SolarSystemView extends View{

    private Paint paintSun, paintEarth, paintOrbitSun, paintOrbitEarth, paintMoon;
    private float earthOrbitDegree = 0f;
    private float moonOrbitDegree = 0f;
    private float orbitalPeriod;
    private boolean animate;
    private float animationSpeed = 1f;

    private GestureDetector gestureDetector;

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

    public void setOrbitalPeriod(float period) {
        this.orbitalPeriod = period;
    }

    public void startAnimation() {
        animate = true;
        new Thread(() -> {
            while (animate) { // Boucle infinie pour l'animation
                float earthRotationIncrement = 360f / orbitalPeriod; // L'incrément de rotation de la Terre par image
                float asteroidRotationIncrement = earthRotationIncrement * 2; // L'astéroïde tourne deux fois plus vite
                earthOrbitDegree += (earthRotationIncrement * animationSpeed); // La vitesse de rotation de la Terre
                if (earthOrbitDegree > 360) earthOrbitDegree -= 360;

                moonOrbitDegree += (asteroidRotationIncrement * animationSpeed); // La vitesse de rotation de l'astéroïde
                if (moonOrbitDegree > 360) moonOrbitDegree -= 360;
                // Demander à la vue de se redessiner
                postInvalidate();

                try {
                    Thread.sleep(16); // Pause pour un taux de rafraîchissement d'environ 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopAnimation(){
        animate = false;
    }

    public boolean isAnimate(){
        return animate;
    }

    private void init(AttributeSet attrs, int defStyle) {
        gestureDetector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAnimate()) {
                    stopAnimation();
                } else {
                    startAnimation();
                }
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Votre logique pour le tap simple ici
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Logique de défilement
                if (e1 != null && e2 != null) {
                    if(animationSpeed >= 0f){
                        animationSpeed -= (distanceY/100f);
                    }
                    else{
                        animationSpeed = 0f;
                    }
                }
                return true;
            }
        });
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
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
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
        float orbitEarthRadius = height / 3f; // Rayon de l'orbite de la Terre
        canvas.drawCircle(width / 2f, height / 2f, orbitEarthRadius, paintOrbitEarth);

        // Dessiner la Terre sur son orbite autour du Soleil
        float earthRadius = height / 25f; // Taille de la Terre
        float earthX = (float) (width / 2 + orbitEarthRadius * Math.cos(Math.toRadians(earthOrbitDegree)));
        float earthY = (float) (height / 2 + orbitEarthRadius * Math.sin(Math.toRadians(earthOrbitDegree)));
        canvas.drawCircle(earthX, earthY, earthRadius, paintEarth);

        // Dessiner l'orbite de la Lune autour de la Terre
        float orbitMoonRadius = earthRadius * 2; // La Lune est plus proche de la Terre dans notre modèle
        canvas.drawCircle(earthX, earthY, orbitMoonRadius, paintOrbitEarth); // Utiliser paintOrbitEarth pour la simplicité

        // Dessiner la Lune sur son orbite autour de la Terre
        float moonRadius = earthRadius / 3; // Taille de la Lune par rapport à celle de la Terre
        float moonX = (float) (earthX + orbitMoonRadius * Math.cos(Math.toRadians(moonOrbitDegree))); // Choisir un angle pour positionner la Lune
        float moonY = (float) (earthY + orbitMoonRadius * Math.sin(Math.toRadians(moonOrbitDegree)));
        canvas.drawCircle(moonX, moonY, moonRadius, paintMoon);
    }
}


