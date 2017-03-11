package ru.merkulyevsasha.gosduma.presentation.deputyrequests;


import android.os.Bundle;

import com.google.firebase.crash.FirebaseCrash;

import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.ui.DialogHelper;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class DeputyRequestsPresenter implements MvpPresenter {

    private final static int NAME_INDEX = 0;
    private final static int REQUESTDATE_INDEX = 1;
    private final static int INITIATOR_INDEX = 2;

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

    public int getCurrentSortIndexValue(){
        return mSort;
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

    public void sort(int oldSort, int sort) {
        mSort = sort;
        mSortDirection = DatabaseHelper.getSortDirection(oldSort, mSort, mSortDirection);
        load();
    }

    public void filter(List<Integer> filter) {

    }

    public void load(){
        inter.loadDeputyRequests(mSearchText, mSortColumn.get(mSort) + mSortDirection, new DeputyRequestsInteractor.DeputyRequestsCallback() {
            @Override
            public void success(List<DeputyRequest> items) {
                if (view == null)
                    return;

                view.hideProgress();
                if (items.size() > 0) {
                    view.showData(items);
                } else {
                    view.showDataEmptyMessage();
                }
            }

            @Override
            public void failure(Exception e) {
                FirebaseCrash.report(e);

                if (view == null)
                    return;

                view.hideProgress();
                view.showDataEmptyMessage();
                //view.showMessage();
            }
        });
    }

    public Bundle getState(){
        Bundle state = new Bundle();

        state.putInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    public void restoreState(Bundle outState){

        if (outState != null){
            mSort = outState.getInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE);
            mSearchText = outState.getString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE);
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
