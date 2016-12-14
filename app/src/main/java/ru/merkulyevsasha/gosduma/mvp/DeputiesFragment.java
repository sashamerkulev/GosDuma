package ru.merkulyevsasha.gosduma.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;


public class DeputiesFragment extends Fragment implements ViewInterface{

    private final static String KEY_POSITION = "POSITION";

    private RecyclerView mRecyclerView;
    private DeputiesRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mPosition = -1;

    private DeputiesPresenter mPresenter;

    public DeputiesFragment(){ }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new DeputiesPresenter(context, this);
        ((ViewInterface.OnViewListener)getActivity()).onPresenterCreated(mPresenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_deputies);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Deputy> items = mPresenter.getDeputies();

        mAdapter = new DeputiesRecyclerViewAdapter(items, ((ViewInterface.onDeputyClickListener)getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        if (mPosition > 0){
            mRecyclerView.scrollToPosition(mPosition);
        }

        return rootView;
    }


    @Override
    public void show(List<Deputy> items){
        mAdapter.mItems = items;
        mAdapter.notifyDataSetChanged();
    }

}
