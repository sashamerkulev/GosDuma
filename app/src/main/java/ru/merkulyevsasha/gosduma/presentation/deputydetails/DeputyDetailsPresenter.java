package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;
import ru.merkulyevsasha.gosduma.ui.DialogHelper;


public class DeputyDetailsPresenter implements MvpPresenter {

    private final static int NAME_INDEX = 0;
    private final static int NUMBER_INDEX = 1;
    private final static int DATE_INDEX = 2;

    private int mDeputyId;
    protected HashMap<Integer, String> mSortColumn;

    protected int mSort;
    protected String mSortDirection;
    protected String mSearchText;

    private DeputyDetailsView view;

    private DeputyDetailsInteractor inter;

    public DeputyDetailsPresenter(DeputyDetailsInteractor inter){

        this.inter = inter;

        mSearchText = "";

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "l.name");
        mSortColumn.put(NUMBER_INDEX, "l.number");
        mSortColumn.put(DATE_INDEX, "l.introductionDate");
    }

    public void search(String searchText) {
        mSearchText = searchText;
        load(mDeputyId);
    }

    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        load(mDeputyId);
    }

    public void load(int deputyId){
        mDeputyId = deputyId;
        view.showProgress();
        inter.loadDeputyLaws(deputyId, mSearchText, mSortColumn.get(mSort) + mSortDirection, new DeputyDetailsInteractor.DeputyDetailsCallback() {
            @Override
            public void success(List<Law> items) {
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

    public Bundle getState() {
        Bundle state = new Bundle();

        state.putInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        state.putInt(KeysBundleHolder.KEY_DEPUTY_ID, mDeputyId);
        return state;
    }

    public void restoreState(Bundle outState) {
        if (outState != null){
            mSort = outState.getInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE);
            mSearchText = outState.getString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE);
            mDeputyId = outState.getInt(KeysBundleHolder.KEY_DEPUTY_ID);
        }
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (DeputyDetailsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    public int getSortDialogType() {
        return DialogHelper.IDD_LAWS_SORT;
    }

    public List<Integer> getCurrentSortIndexValue() {

        List<Integer> result = new ArrayList<>();
        result.add(mSort);
        return result;
    }


}
