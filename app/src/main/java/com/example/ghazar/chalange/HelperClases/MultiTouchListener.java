package com.example.ghazar.chalange.HelperClases;

import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ghazar.chalange.Activitys.MainActivity;

/**
 * Created by ghazar on 12/7/17.
 */

public class MultiTouchListener implements View.OnTouchListener {
    private float mPrevX;
    private float mPrevY;
    private float mCurentX;
    private float mCurentY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = event.getX();
                mPrevY = event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                mCurentX = event.getRawX();
                mCurentY = event.getRawY();

                ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(v.getLayoutParams());
                marginParams.setMargins((int)(mCurentX - mPrevX), (int)(mCurentY - mPrevY),0, 0);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                v.setLayoutParams(layoutParams);

                break;
            }
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}