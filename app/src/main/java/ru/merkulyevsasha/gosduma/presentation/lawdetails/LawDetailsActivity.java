package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import android.os.Bundle;

import ru.merkulyevsasha.gosduma.R;


public class LawDetailsActivity extends BaseLawDetailsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLargeLandscape()) {
            finish();
            return;
        }

        initActivity(savedInstanceState);
        setTitle(R.string.menu_laws);
    }

}
