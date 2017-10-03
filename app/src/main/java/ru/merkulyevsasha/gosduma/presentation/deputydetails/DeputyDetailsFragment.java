package ru.merkulyevsasha.gosduma.presentation.deputydetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.MvpView;
import ru.merkulyevsasha.gosduma.presentation.commons.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsView;


public class DeputyDetailsFragment extends Fragment implements DeputyDetailsView {

    private Deputy mDeputy;

    private LawsRecyclerViewAdapter mAdapter;

    @Inject
    DeputyDetailsPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        GosDumaApp.getComponent().inject(this);
    }

    public static DeputyDetailsFragment newInstance(Deputy deputy) {
        DeputyDetailsFragment fragment = new DeputyDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KeysBundleHolder.KEY_DEPUTY, deputy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deputy_details, container, false);

        mDeputy = getArguments().getParcelable(KeysBundleHolder.KEY_DEPUTY);

        TextView mDeputyName = (TextView)v.findViewById(R.id.textview_deputy_name);
        TextView mDeputyPosition = (TextView)v.findViewById(R.id.textview_position);
        TextView mFractionName = (TextView)v.findViewById(R.id.textview_deputy_fractionName);
        TextView mFractionRole = (TextView)v.findViewById(R.id.textview_deputy_fractionRole);
        TextView mDeputyRanks = (TextView)v.findViewById(R.id.textview_deputy_ranks);

        assert mDeputy != null;
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

        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);

        mAdapter = new LawsRecyclerViewAdapter(new ArrayList<Law>(), new LawsRecyclerViewAdapter.OnLawClickListener() {
            @Override
            public void onLawClick(Law law) {

            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart(this);
            mPresenter.load(mDeputy.id);
        }
    }

    @Override
    public void showData(final List<Law> items){
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setItems(items);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void showDataEmptyMessage() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    recyclerView.setVisibility(View.GONE);
//                    emptyLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void showLawDetailsScreen(Law law) {

    }

    @Override
    public void share(Deputy deputy) {

    }

    @Override
    public void showSortDialog(int currentItemIndex) {

    }

    @Override
    public void showMessage(int resId) {
        if (isAdded()){
            ((MvpView)getActivity()).showMessage(resId);
        }
    }

    @Override
    public void hideProgress() {
        if (isAdded()){
            ((MvpView)getActivity()).hideProgress();
        }
    }

    @Override
    public void showProgress() {
        if (isAdded()){
            ((MvpView)getActivity()).showProgress();
        }
    }

}
