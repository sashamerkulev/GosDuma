package ru.merkulyevsasha.gosduma;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.merkulyevsasha.gosduma.models.Deputy;


public class DeputyDetailsFragment extends Fragment {

    public static DeputyDetailsFragment newInstance(Deputy deputy) {
        DeputyDetailsFragment details = new DeputyDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("deputy", deputy);
        details.setArguments(args);
        return details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deputydetails, container, false);

        final Deputy deputy = getArguments().getParcelable("deputy");

        return v;
    }

}
