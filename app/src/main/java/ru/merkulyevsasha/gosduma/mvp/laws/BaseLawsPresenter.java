package ru.merkulyevsasha.gosduma.mvp.laws;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.DialogHelper;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;


public class BaseLawsPresenter implements PresenterInterface {

    final static int NAME_INDEX = 0;
    final static int NUMBER_INDEX = 1;
    final static int DATE_INDEX = 2;

    private final static String KEY_CURRENT_SORT_VALUE = "SORT";
    private final static String KEY_CURRENT_SORT_DIRECTIONVALUE = "SORT_DIRECTION";

    final Context mContext;
    final LawsViewInterface mViewInterface;
    HashMap<Integer, String> mSortColumn;

    int mSort;
    String mSortDirection;
    String mSearchText;

    BaseLawsPresenter(Context context, LawsViewInterface viewInterface){

        mContext = context;
        mViewInterface = viewInterface;

        mSearchText = "";

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSortColumn = new HashMap<Integer, String>();
        mSortColumn.put(NAME_INDEX, "l.name");
        mSortColumn.put(NUMBER_INDEX, "l.number");
        mSortColumn.put(DATE_INDEX, "l.introductionDate");

    }

    @Override
    public int getSortDialogType() {
        return DialogHelper.IDD_LAWS_SORT;
    }

    @Override
    public List<Integer> getCurrentSortIndexValue() {

        List<Integer> result = new ArrayList<Integer>();
        result.add(mSort);
        return result;
    }

    @Override
    public boolean isSortMenuVisible() {
        return true;
    }

    @Override
    public int getFilterDialogType() {
        return 0;
    }

    @Override
    public List<Integer> getCurrentFilterIndexValue() {
        return null;
    }

    @Override
    public boolean isFilterMenuVisible() {
        return false;
    }

    @Override
    public void search(String searchText) {

    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {

    }

    @Override
    public void filter(List<Integer> filter) {

    }

    @Override
    public Bundle getState() {
        Bundle state = new Bundle();

        state.putInt(KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);

        return state;
    }

    @Override
    public void restoreState(Bundle outState) {
        if (outState != null){
            mSort = outState.getInt(KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KEY_CURRENT_SORT_DIRECTIONVALUE);
        }
    }
}