package ru.merkulyevsasha.gosduma.presentation.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;


public class NewsDetailsFragment extends Fragment {

    @BindView(R.id.textview_newsdetailstopic) TextView textViewTopic;
    @BindView(R.id.textview_newsdetailsdescription) TextView textViewDescription;

    public static NewsDetailsFragment newInstance(String topic, String description) {
        NewsDetailsFragment details = new NewsDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KeysBundleHolder.KEY_TOPIC, topic);
        args.putString(KeysBundleHolder.KEY_DESCRIPTION, description);
        details.setArguments(args);
        return details;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_details, container, false);
        ButterKnife.bind(this, v);
        if (getArguments() == null) return v;
        final String topic = getArguments().getString(KeysBundleHolder.KEY_TOPIC);
        final String description = getArguments().getString(KeysBundleHolder.KEY_DESCRIPTION);

        textViewTopic.setText(topic);
        textViewDescription.setText(description);

        return v;
    }

}
