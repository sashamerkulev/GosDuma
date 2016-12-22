package ru.merkulyevsasha.gosduma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


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
        final String topic = intent.getStringExtra("topic");
        final String description = intent.getStringExtra("description");

        TextView textViewTopic = (TextView)findViewById(R.id.textview_newsdetailstopic);
        TextView textViewDescription = (TextView)findViewById(R.id.textview_newsdetailsdescription);

        textViewTopic.setText(topic);
        textViewDescription.setText(description);
    }

}
