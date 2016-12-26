package ru.merkulyevsasha.gosduma.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;

import static ru.merkulyevsasha.gosduma.MainActivity.KEY_NAME;
import static ru.merkulyevsasha.gosduma.news.NewsActivity.KEY_DESCRIPTION;
import static ru.merkulyevsasha.gosduma.news.NewsActivity.KEY_TOPIC;

public class NewsDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLargeLandscape()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_newsdetails);

        initSupportActionBarWithBackButton(R.id.newsdetails_toolbar);

        Intent intent = getIntent();
        final String topic = intent.getStringExtra(KEY_TOPIC);
        final String description = intent.getStringExtra(KEY_DESCRIPTION);
        final String name = intent.getStringExtra(KEY_NAME);
        setTitle(name);

        TextView textViewTopic = (TextView)findViewById(R.id.textview_newsdetailstopic);
        TextView textViewDescription = (TextView)findViewById(R.id.textview_newsdetailsdescription);

        textViewTopic.setText(topic);
        textViewDescription.setText(description);
    }

}
