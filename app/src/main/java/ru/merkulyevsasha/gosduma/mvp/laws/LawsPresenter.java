package ru.merkulyevsasha.gosduma.mvp.laws;


import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;


public class LawsPresenter extends BaseLawsPresenter {

    LawsViewInterface mViewInterface;


    public LawsPresenter(Activity context, LawsViewInterface viewInterface) {
        super(context);

        mViewInterface = viewInterface;

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(NUMBER_INDEX, "number");
        mSortColumn.put(DATE_INDEX, "introductionDate");
    }

    public List<Law> getLaws(){
        if (mSearchText.isEmpty()) {
            return new ArrayList<>();
        }
        return DatabaseHelper.getInstance(mContext).getLaws(mSearchText, mSortColumn.get(mSort) + mSortDirection);
    }

    private void runThread(){
        ((ViewInterface)mContext).showProgress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Law> items = getLaws();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewInterface)mContext).hideProgress();
                        mViewInterface.show(items);
                    }
                });
            }
        }).start();
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;
        if (searchText.isEmpty()){
            mViewInterface.show(new ArrayList<Law>());
        } else {
            runThread();
        }
    }

    @Override
    public boolean isSortMenuVisible() {
        return !mSearchText.isEmpty();
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        runThread();
    }

}
