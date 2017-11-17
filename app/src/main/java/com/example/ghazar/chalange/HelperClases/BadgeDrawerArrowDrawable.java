package com.example.ghazar.chalange.HelperClases;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import java.util.Objects;


public class BadgeDrawerArrowDrawable extends DrawerArrowDrawable {
    private static final float SIZE_FACTOR = .3f;
    private static final float HALF_SIZE_FACTOR = SIZE_FACTOR / 2;

    private Paint backgroundPaint;
    private Paint textPaint;
//    private String text;
    private int events;
    private boolean enabled = true;

    public BadgeDrawerArrowDrawable(Context context) {
        super(context);
        events = 0;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(SIZE_FACTOR * getIntrinsicHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!enabled) {
            return;
        }

        final Rect bounds = getBounds();
        final float x = (1 - HALF_SIZE_FACTOR) * bounds.width();
        final float y = HALF_SIZE_FACTOR * bounds.height();
        canvas.drawCircle(x, y, SIZE_FACTOR * bounds.width(), backgroundPaint);

//        if (text == null || text.length() == 0) {
//            return;
//        }
//
        final Rect textBounds = new Rect();
        textPaint.getTextBounds(Integer.toString(events), 0, Integer.toString(events).length(), textBounds);
        canvas.drawText(Integer.toString(events), x, y + textBounds.height() / 2, textPaint);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            invalidateSelf();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEvents(int events) {
        this.events = events;

        if(events == 0)
            setEnabled(false);
        else
            setEnabled(true);

        invalidateSelf();
    }

    public int getEvents() {
        return events;
    }

    public void pushEvent(int count)
    {
        events += count;

        invalidateSelf();
    }

    public void pullEvent(int count)
    {
        if(count <= events)
            events -= count;
        else
            events = 0;

        if(events == 0)
            setEnabled(false);
        else
            setEnabled(true);

        invalidateSelf();
    }

    public void setBackgroundColor(int color) {
        if (backgroundPaint.getColor() != color) {
            backgroundPaint.setColor(color);
            invalidateSelf();
        }
    }

    public int getBackgroundColor() {
        return backgroundPaint.getColor();
    }

    public void setTextColor(int color) {
        if (textPaint.getColor() != color) {
            textPaint.setColor(color);
            invalidateSelf();
        }
    }

    public int getTextColor() {
        return textPaint.getColor();
    }
}
