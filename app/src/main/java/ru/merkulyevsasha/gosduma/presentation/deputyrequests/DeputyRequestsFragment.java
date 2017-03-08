package ru.merkulyevsasha.gosduma.presentation.deputyrequests;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpFragment;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

import static ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity.KEY_POSITION;


public class DeputyRequestsFragment extends Fragment implements DeputyRequestsView, MvpFragment {


    private LinearLayout mEmptyLayout;

    private RecyclerView mRecyclerView;
    private DeputyRequestsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Inject
    DeputyRequestsPresenter mPresenter;

    private int mPosition = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        GosDumaApp.getComponent().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        Bundle state = mPresenter.getState();
        if (state != null){
            outState.putAll(state);
        }
        outState.putInt(KEY_POSITION, mLayoutManager.findFirstVisibleItemPosition());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            mPresenter.restoreState(savedInstanceState);
            mPosition = savedInstanceState.getInt(KEY_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deputyrequests, container, false);

        mEmptyLayout = (LinearLayout) rootView.findViewById(R.id.layout_empty_search);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_deputies_requests);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DeputyRequestsRecyclerViewAdapter(new ArrayList<DeputyRequest>(), ((OnDeputyRequestsClickListener)getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
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
            mPresenter.load();
        }
    }

    @Override
    public void showData(final List<DeputyRequest> items){
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.mItems = items;
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyLayout.setVisibility(View.GONE);
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
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getSortDialogType() {
        return mPresenter.getSortDialogType();
    }

    @Override
    public List<Integer> getCurrentSortIndexValue() {
        return mPresenter.getCurrentSortIndexValue();
    }

    @Override
    public boolean isSortMenuVisible() {
        return mPresenter.isSortMenuVisible();
    }

    @Override
    public int getFilterDialogType() {
        return mPresenter.getFilterDialogType();
    }

    @Override
    public List<Integer> getCurrentFilterIndexValue() {
        return mPresenter.getCurrentFilterIndexValue();
    }

    @Override
    public boolean isFilterMenuVisible() {
        return mPresenter.isFilterMenuVisible();
    }

    @Override
    public void search(String searchText) {
        mPresenter.search(searchText);
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mPresenter.sort(oldSort, sort);
    }

    @Override
    public void filter(List<Integer> filter) {
        mPresenter.filter(filter);
    }

    @Override
    public Bundle getState() {
        return mPresenter.getState();
    }

    @Override
    public void restoreState(Bundle outState) {
        mPresenter.restoreState(outState);
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
