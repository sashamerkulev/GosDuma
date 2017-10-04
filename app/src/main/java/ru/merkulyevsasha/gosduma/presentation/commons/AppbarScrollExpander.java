package ru.merkulyevsasha.gosduma.presentation.commons;

import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sasha_merkulev on 01.10.2017.
 */

public class AppbarScrollExpander {

    private final TouchPoint touchPoint;
    private boolean expanded;

    public AppbarScrollExpander(View view, final AppBarLayout appbarlayout){
        touchPoint = new TouchPoint();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        touchPoint.y = event.getY();
                        touchPoint.x = event.getX();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        float y = event.getY();
                        //float x = event.getX();
                        expanded = y - touchPoint.y > 0;
                        appbarlayout.setExpanded(expanded);
                        break;
                }
                return false;
            }
        });

    }

    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }

    public boolean getExpanded(){
        return expanded;
    }

    private class TouchPoint{
        float x;
        float y;
    }

}
