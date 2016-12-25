package ru.merkulyevsasha.gosduma.mvp.deputyrequests;


import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.DialogHelper;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;

public class DeputyRequestsPresenter implements PresenterInterface {

    private final static int NAME_INDEX = 0;
    private final static int REQUESTDATE_INDEX = 1;
    private final static int INITIATOR_INDEX = 2;

    private final static String KEY_CURRENT_SORT_VALUE = "SORT";
    private final static String KEY_CURRENT_SORT_DIRECTIONVALUE = "SORT_DIRECTION";
    public final static String KEY_CURRENT_SEARCHTEXT_VALUE = "SEARCH";

    private final Activity mContext;
    private final DeputyRequestsViewInterface mViewInterface;
    private final HashMap<Integer, String> mSortColumn;

    private int mSort;
    private String mSortDirection;
    private String mSearchText;

    public DeputyRequestsPresenter(Activity context, DeputyRequestsViewInterface viewInterface){

        mContext = context;
        mViewInterface = viewInterface;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSearchText = "";

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(REQUESTDATE_INDEX, "requestDate");
        mSortColumn.put(INITIATOR_INDEX, "initiator");
    }

    @Override
    public int getSortDialogType() {
        return DialogHelper.IDD_DEPUTY_REQUEST_SORT;
    }

    @Override
    public List<Integer> getCurrentSortIndexValue(){
        List<Integer> result = new ArrayList<>();
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

    public List<DeputyRequest> getDeputyRequests(){
        return DatabaseHelper.getInstance(mContext).getDeputyRequests(mSearchText, mSortColumn.get(mSort) + mSortDirection);
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;
        mViewInterface.show(getDeputyRequests());
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        mViewInterface.show(getDeputyRequests());
    }

    @Override
    public void filter(List<Integer> filter) {

    }

    @Override
    public Bundle getState(){
        Bundle state = new Bundle();

        state.putInt(KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putString(KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    @Override
    public void restoreState(Bundle outState){

        if (outState != null){
            mSort = outState.getInt(KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KEY_CURRENT_SORT_DIRECTIONVALUE);
            mSearchText = outState.getString(KEY_CURRENT_SEARCHTEXT_VALUE);
        }
    }
}
