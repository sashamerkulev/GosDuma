package ru.merkulyevsasha.gosduma.mvp.laws;


import android.app.Activity;
import android.os.Bundle;

import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;


public class DeputyLawsPresenter extends BaseLawsPresenter {

    private final static String KEY_DEPUTY_ID = "DEPUTY_ID";

    private int mDeputyId;

    LawsViewInterface mViewInterface;

    public DeputyLawsPresenter(Activity context, LawsViewInterface viewInterface){
        super(context);
        mViewInterface = viewInterface;
    }

    public List<Law> getDeputyLaws(int deputyId){
        mDeputyId = deputyId;
        return DatabaseHelper.getInstance(mContext).getDeputyLaws(deputyId, mSearchText, mSortColumn.get(mSort) + mSortDirection);
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;
        mViewInterface.show(getDeputyLaws(mDeputyId));
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        mViewInterface.show(getDeputyLaws(mDeputyId));
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
