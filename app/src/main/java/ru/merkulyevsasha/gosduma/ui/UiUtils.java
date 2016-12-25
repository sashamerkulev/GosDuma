package ru.merkulyevsasha.gosduma.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class UiUtils {

    public static void setTextToTextViewOrGone(String text, TextView textView){
        if (text == null || text.isEmpty()){
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
        }
    }

    public static void setTextToTextViewOrLayoutGone(String text, TextView textView, LinearLayout layout){
        if (text == null || text.isEmpty()){
            layout.setVisibility(View.GONE);
        } else {
            textView.setText(text);
        }
    }


}
