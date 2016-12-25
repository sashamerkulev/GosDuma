package ru.merkulyevsasha.gosduma.mvp.deputies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.mvp.DeputiesViewInterface;
import ru.merkulyevsasha.gosduma.mvp.OnDeputyClickListener;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

import static ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsActivity.KEY_POSITION;


public class DeputiesFragment extends Fragment
        implements
         DeputiesViewInterface{

    private LinearLayout mEmptyLayout;

    private RecyclerView mRecyclerView;
    private DeputiesRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mPosition = -1;

    private DeputiesPresenter mPresenter;

    public DeputiesFragment(){ }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new DeputiesPresenter(getActivity(), this);
        ((ViewInterface.OnPresenterListener)getActivity()).onPresenterCreated(mPresenter);
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
        View rootView = inflater.inflate(R.layout.fragment_deputies, container, false);

        mEmptyLayout = (LinearLayout) rootView.findViewById(R.id.layout_empty_search);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_deputies);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Deputy> items = mPresenter.getDeputies();

        mAdapter = new DeputiesRecyclerViewAdapter(items, ((OnDeputyClickListener)getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        showData(items.size() > 0);

        return rootView;
    }

    private void showData(boolean show){
        if (show) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show(List<Deputy> items){
        mAdapter.mItems = items;
        mAdapter.notifyDataSetChanged();
        showData(items.size() > 0);
    }

}
