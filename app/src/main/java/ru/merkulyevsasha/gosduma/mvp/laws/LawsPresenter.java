package ru.merkulyevsasha.gosduma.mvp.laws;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;


public class LawsPresenter extends BaseLawsPresenter {

    public LawsPresenter(Context context, LawsViewInterface viewInterface) {
        super(context, viewInterface);

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(NUMBER_INDEX, "number");
        mSortColumn.put(DATE_INDEX, "introductionDate");
    }

    public List<Law> getLaws(){
        return DatabaseHelper.getInstance(mContext).getLaws(mSearchText, mSortColumn.get(mSort) + mSortDirection);
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;

        if (searchText.isEmpty()){
            mViewInterface.show(new ArrayList<Law>());
        } else {
            mViewInterface.show(getLaws());
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
        mViewInterface.show(getLaws());
    }

}
