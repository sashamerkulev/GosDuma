package ru.merkulyevsasha.gosduma.presentation.deputyrequests;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.DialogHelper;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsView;


public class DeputyRequestsPresenter implements MvpPresenter {

    private final static int NAME_INDEX = 0;
    private final static int REQUESTDATE_INDEX = 1;
    private final static int INITIATOR_INDEX = 2;

    private final static String KEY_CURRENT_SORT_VALUE = "SORT";
    private final static String KEY_CURRENT_SORT_DIRECTIONVALUE = "SORT_DIRECTION";
    private final static String KEY_CURRENT_SEARCHTEXT_VALUE = "SEARCH";

    private DeputyRequestsInteractor inter;
    private DeputyRequestsView view;

    private final HashMap<Integer, String> mSortColumn;

    private int mSort;
    private String mSortDirection;
    private String mSearchText;

    public DeputyRequestsPresenter(DeputyRequestsInteractor inter){

        this.inter = inter;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSearchText = "";

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(REQUESTDATE_INDEX, "requestDate");
        mSortColumn.put(INITIATOR_INDEX, "initiator");
    }

    public int getSortDialogType() {
        return DialogHelper.IDD_DEPUTY_REQUEST_SORT;
    }

    public List<Integer> getCurrentSortIndexValue(){
        List<Integer> result = new ArrayList<>();
        result.add(mSort);
        return result;
    }

    public boolean isSortMenuVisible() {
        return true;
    }

    public int getFilterDialogType() {
        return 0;
    }

    public List<Integer> getCurrentFilterIndexValue() {
        return null;
    }

    public boolean isFilterMenuVisible() {
        return false;
    }

    public void search(String searchText) {
        mSearchText = searchText;
        load();
    }

    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        load();
    }

    public void filter(List<Integer> filter) {

    }

    public void load(){
        inter.loadDeputyRequests(mSearchText, mSortColumn.get(mSort) + mSortDirection, new DeputyRequestsInteractor.DeputyRequestsCallback() {
            @Override
            public void success(List<DeputyRequest> items) {
                view.hideProgress();
                if (items.size() > 0) {
                    view.showData(items);
                } else {
                    view.showDataEmptyMessage();
                }
            }

            @Override
            public void failure(Exception e) {
                view.hideProgress();
                view.showDataEmptyMessage();
                //view.showMessage();
            }
        });
    }

    public Bundle getState(){
        Bundle state = new Bundle();

        state.putInt(KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putString(KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    public void restoreState(Bundle outState){

        if (outState != null){
            mSort = outState.getInt(KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KEY_CURRENT_SORT_DIRECTIONVALUE);
            mSearchText = outState.getString(KEY_CURRENT_SEARCHTEXT_VALUE);
        }
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (DeputyRequestsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }
}
