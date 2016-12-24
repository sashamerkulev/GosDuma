package ru.merkulyevsasha.gosduma.mvp.laws;

import android.os.Bundle;


public class LawDetailsActivity extends BaseLawDetailsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLargeLandscape()) {
            finish();
            return;
        }

        initActivity(savedInstanceState);
    }

}
