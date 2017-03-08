package ru.merkulyevsasha.gosduma.presentation.deputies;


import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsView;
import ru.merkulyevsasha.gosduma.presentation.laws.BaseLawsPresenter;


public class DeputyLawsPresenter extends BaseLawsPresenter {

    private final static String KEY_DEPUTY_ID = "DEPUTY_ID";

    private int mDeputyId;

    private final LawsView mViewInterface;

    public DeputyLawsPresenter(Activity context, LawsView viewInterface){
        super(context);
        mViewInterface = viewInterface;
    }

    public List<Law> getDeputyLaws(int deputyId){
        mDeputyId = deputyId;
        return mDatabase.getDeputyLaws(deputyId, mSearchText, mSortColumn.get(mSort) + mSortDirection);
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;
        mViewInterface.showData(getDeputyLaws(mDeputyId));
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        mViewInterface.showData(getDeputyLaws(mDeputyId));
    }

    @Override
    public Bundle getState() {
        Bundle state = super.getState();
        state.putInt(KEY_DEPUTY_ID, mDeputyId);
        return state;
    }

    @Override
    public void restoreState(Bundle outState) {
        if (outState != null){
            super.restoreState(outState);
            mDeputyId = outState.getInt(KEY_DEPUTY_ID);
        }
    }


}
