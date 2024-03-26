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

import com.example.asteroidedetector.model.AsteroidDetailModel;

public class SolarSystemView extends View{

    private Paint paintSun, paintEarth, paintOrbitSun, paintOrbitEarth, paintMoon;
    private boolean animated;
    private int frame;
    private int delayMs = 60;
    private GestureDetector gestureDetector;
    private AsteroidDetailModel asteroid;

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

    public void setAsteroid(AsteroidDetailModel asteroid){
        this.asteroid = asteroid;
        this.invalidate();
    }

    private void init(AttributeSet attrs, int defStyle) {
        animated = true;
        gestureDetector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {
                animated = !animated;
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                // Logique de défilement
                if (e1 != null) {
                    delayMs = distanceY<0 ? delayMs-1 : delayMs+1;
                    if(delayMs<=0){
                        delayMs=1;
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
        float width = getWidth();
        float height = getHeight();

        canvas.translate(width/2, height/2);
        canvas.drawCircle(0, 0, 100, paintSun);
        canvas.drawCircle(0, 0, width/2 - 50, paintOrbitEarth);
        canvas.save();
        canvas.rotate(frame % 360, 0, 0);
        canvas.translate(width/2 - 50, 0);
        canvas.drawCircle(0, 0, 50, paintEarth);

        if (asteroid != null) {
            int DISTANCE_EARTH_SUN = 149597870;
            float radiusAsteroid = (asteroid.getDistance() * (width / 2 - 50) / DISTANCE_EARTH_SUN);
            canvas.drawCircle(0, 0, radiusAsteroid, paintOrbitSun);
            canvas.save();
            canvas.rotate((frame * (asteroid.getOrbitalPeriod() / 10f)) % 360, 0, 0);
            canvas.drawCircle(-radiusAsteroid, 0, 20, paintMoon);
        }
        canvas.restore();

        postDelayed(() -> {
            if(animated){
                frame++;
            }
            invalidate();
        }, delayMs);
    }
}


