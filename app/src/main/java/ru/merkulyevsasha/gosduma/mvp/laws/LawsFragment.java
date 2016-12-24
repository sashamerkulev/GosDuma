package ru.merkulyevsasha.gosduma.mvp.laws;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;
import ru.merkulyevsasha.gosduma.mvp.OnLawClickListener;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

import static ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsActivity.KEY_POSITION;


public class LawsFragment extends Fragment
        implements
        LawsViewInterface {

    private LawsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mPosition = -1;

    private LawsPresenter mPresenter;

    public LawsFragment(){ }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new LawsPresenter(context, this);
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
        View rootView = inflater.inflate(R.layout.fragment_laws, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_laws);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LawsRecyclerViewAdapter(new ArrayList<Law>(), ((OnLawClickListener)getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        if (mPosition > 0){
            mRecyclerView.scrollToPosition(mPosition);
        }

        return rootView;
    }


    @Override
    public void show(List<Law> items){
        mAdapter.mItems = items;
        mAdapter.notifyDataSetChanged();
    }

}
