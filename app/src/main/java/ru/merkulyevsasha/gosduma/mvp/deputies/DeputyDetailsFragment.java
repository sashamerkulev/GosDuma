package ru.merkulyevsasha.gosduma.mvp.deputies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsPresenter;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsRecyclerViewAdapter;


public class DeputyDetailsFragment extends Fragment
    implements ViewInterface{


    private LawsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

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

        ButterKnife.bind(v);

        final Deputy mDeputy = getArguments().getParcelable("deputy");

        TextView mDeputyName = (TextView)v.findViewById(R.id.textview_deputy_name);
        TextView mDeputyPosition = (TextView)v.findViewById(R.id.textview_position);
        TextView mFractionName = (TextView)v.findViewById(R.id.textview_deputy_fractionName);
        TextView mFractionRole = (TextView)v.findViewById(R.id.textview_deputy_fractionRole);
        TextView mDeputyRanks = (TextView)v.findViewById(R.id.textview_deputy_ranks);

        final StringBuilder position = new StringBuilder();
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (mDeputy.credentialsStart > 0) {
            position.append(mDeputy.position);
            position.append(" (период с ");
            position.append(format.format(new Date(mDeputy.credentialsStart)));
            if (mDeputy.credentialsEnd > 0) {
                position.append(" по " + format.format(new Date(mDeputy.credentialsStart)));
            }
            position.append(")");
        } else{
            position.append(mDeputy.position);
        }
        mDeputyPosition.setText(position.toString());

        final StringBuilder name = new StringBuilder();
        if (mDeputy.birthdate > 0) {
            name.append(mDeputy.name);
            name.append(" (" + format.format(new Date(mDeputy.birthdate)) + ")");
        } else {
            name.append(mDeputy.name);
        }
        mDeputyName.setText(name.toString());

        final StringBuilder ranks = new StringBuilder();
        if (mDeputy.degrees.isEmpty()){
            mDeputyRanks.setVisibility(View.GONE);
        } else {
            ranks.append(mDeputy.ranks.isEmpty()? mDeputy.degrees : mDeputy.degrees + " (" + mDeputy.ranks + ")");
            mDeputyRanks.setText(ranks);
        }

        mFractionName.setText(mDeputy.fractionName);
        mFractionRole.setText(mDeputy.fractionRole + " " + mDeputy.fractionRegion);

        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerview_deputy_laws);

        LawsPresenter mPresenter = new LawsPresenter(getActivity(), this);

        List<Law> items = mPresenter.getDeputyLaws(mDeputy.id);
        mAdapter = new LawsRecyclerViewAdapter(items, (ViewInterface.OnLawClickListener)getActivity());;

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void show(List<Deputy> items) {

    }
}
