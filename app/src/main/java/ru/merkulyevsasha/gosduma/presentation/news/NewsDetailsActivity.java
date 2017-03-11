package ru.merkulyevsasha.gosduma.presentation.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;


public class NewsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_newsdetails);

        Toolbar toolbar = (Toolbar) findViewById(R.id.newsdetails_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        final String topic = intent.getStringExtra(KeysBundleHolder.KEY_TOPIC);
        final String description = intent.getStringExtra(KeysBundleHolder.KEY_DESCRIPTION);
        final String name = intent.getStringExtra(KeysBundleHolder.KEY_NAME);
        setTitle(name);

        TextView textViewTopic = (TextView)findViewById(R.id.textview_newsdetailstopic);
        TextView textViewDescription = (TextView)findViewById(R.id.textview_newsdetailsdescription);

        textViewTopic.setText(topic);
        textViewDescription.setText(description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
