package ru.merkulyevsasha.gosduma.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.merkulyevsasha.gosduma.R;

import static ru.merkulyevsasha.gosduma.news.NewsActivity.KEY_DESCRIPTION;
import static ru.merkulyevsasha.gosduma.news.NewsActivity.KEY_TOPIC;


public class NewsDetailsFragment extends Fragment {

    public static NewsDetailsFragment newInstance(String topic, String description) {
        NewsDetailsFragment details = new NewsDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TOPIC, topic);
        args.putString(KEY_DESCRIPTION, description);
        details.setArguments(args);
        return details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_details, container, false);

        TextView textViewTopic = (TextView)v.findViewById(R.id.textview_newsdetailstopic);
        TextView textViewDescription = (TextView)v.findViewById(R.id.textview_newsdetailsdescription);

        final String topic = getArguments().getString(KEY_TOPIC);
        final String description = getArguments().getString(KEY_DESCRIPTION);

        textViewTopic.setText(topic);
        textViewDescription.setText(description);

        return v;
    }

}
