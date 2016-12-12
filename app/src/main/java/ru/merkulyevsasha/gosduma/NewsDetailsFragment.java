package ru.merkulyevsasha.gosduma;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NewsDetailsFragment extends Fragment {

    public static NewsDetailsFragment newInstance(String topic, String description) {
        NewsDetailsFragment details = new NewsDetailsFragment();
        Bundle args = new Bundle();
        args.putString("topic", topic);
        args.putString("description", description);
        details.setArguments(args);
        return details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newsdetails, container, false);

        TextView textViewTopic = (TextView)v.findViewById(R.id.textview_newsdetailstopic);
        TextView textViewDescription = (TextView)v.findViewById(R.id.textview_newsdetailsdescription);

        final String topic = getArguments().getString("topic");
        final String description = getArguments().getString("description");

        textViewTopic.setText(topic);
        textViewDescription.setText(description);

        return v;
    }

}
