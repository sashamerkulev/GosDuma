package ru.merkulyevsasha.gosduma.mvp.lawdetails;

import android.os.Bundle;

import ru.merkulyevsasha.gosduma.R;

public class DeputyLawDetailsActivity extends BaseLawDetailsActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity(savedInstanceState);
        setTitle(R.string.menu_laws);

    }

}
