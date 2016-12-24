package ru.merkulyevsasha.gosduma.mvp.deputies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;
import ru.merkulyevsasha.gosduma.mvp.OnLawClickListener;
import ru.merkulyevsasha.gosduma.mvp.laws.DeputyLawsPresenter;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsRecyclerViewAdapter;


public class DeputyDetailsFragment extends Fragment
    implements LawsViewInterface {

    private final static String KEY_DEPUTY = "deputy";

    public static DeputyDetailsFragment newInstance(Deputy deputy) {
        DeputyDetailsFragment details = new DeputyDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_DEPUTY, deputy);
        details.setArguments(args);
        return details;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deputydetails, container, false);

        ButterKnife.bind(v);

        final Deputy mDeputy = getArguments().getParcelable(KEY_DEPUTY);

        TextView mDeputyName = (TextView)v.findViewById(R.id.textview_deputy_name);
        TextView mDeputyPosition = (TextView)v.findViewById(R.id.textview_position);
        TextView mFractionName = (TextView)v.findViewById(R.id.textview_deputy_fractionName);
        TextView mFractionRole = (TextView)v.findViewById(R.id.textview_deputy_fractionRole);
        TextView mDeputyRanks = (TextView)v.findViewById(R.id.textview_deputy_ranks);

        mDeputyName.setText(mDeputy.getNameWithBirthday());

        mDeputyPosition.setText(mDeputy.getPositionWithStartAndEndDates());

        String ranks = mDeputy.getRanksWithDegrees();
        if (ranks.isEmpty()){
            mDeputyRanks.setVisibility(View.GONE);
        } else {
            mDeputyRanks.setText(ranks);
        }

        mFractionName.setText(mDeputy.fractionName);
        mFractionRole.setText(mDeputy.fractionRole + " " + mDeputy.fractionRegion);

        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerview_laws);

        DeputyLawsPresenter mPresenter = new DeputyLawsPresenter(getActivity(), this);

        List<Law> items = mPresenter.getDeputyLaws(mDeputy.id);
        LawsRecyclerViewAdapter mAdapter = new LawsRecyclerViewAdapter(items, (OnLawClickListener) getActivity());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void show(List<Law> items) {

    }
}
