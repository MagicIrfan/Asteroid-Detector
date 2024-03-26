package com.example.asteroidedetector.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.asteroidedetector.R;

public class CustomCircleView extends View {

    private Paint paint;
    private int circleColor;

    public CustomCircleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load the custom attributes and set them accordingly
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCircleView, defStyle, 0);

        circleColor = a.getColor(R.styleable.CustomCircleView_circleColor, Color.BLUE); // Default color is blue

        a.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = Math.min(getWidth(), getHeight()) / 2f;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
    }
}