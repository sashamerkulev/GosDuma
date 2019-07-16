package ru.merkulyevsasha.gdcoreandroid;

import android.content.res.Configuration;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class UiUtils {

    public static void setTextToTextViewOrLayoutGone(String text, TextView textView, LinearLayout layout){
        if (text == null || text.isEmpty()){
            layout.setVisibility(View.GONE);
        } else {
            textView.setText(text);
        }
    }

    private static boolean isLarge(AppCompatActivity activity) {
        return (activity.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isLargeLandscape(AppCompatActivity activity){
        return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isLarge(activity);
    }



}
