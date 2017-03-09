package ru.merkulyevsasha.gosduma.presentation.laws;


import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.ui.DialogHelper;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class LawsPresenter implements MvpPresenter{

    final static int NAME_INDEX = 0;
    final static int NUMBER_INDEX = 1;
    final static int DATE_INDEX = 2;


    protected int mSort;
    protected String mSortDirection;
    protected String mSearchText;
    protected HashMap<Integer, String> mSortColumn;

    private LawsView view;
    private LawsInteractor inter;

    public LawsPresenter(LawsInteractor inter) {

        this.inter = inter;

        mSearchText = "";

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(NUMBER_INDEX, "number");
        mSortColumn.put(DATE_INDEX, "introductionDate");
    }

    public Bundle getState() {
        Bundle state = new Bundle();

        state.putInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    public void restoreState(Bundle outState) {
        if (outState != null){
            mSort = outState.getInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE);
            mSearchText = outState.getString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE);
        }
    }

    public void load(){
        if (mSearchText.isEmpty()){
            //view.showData(new ArrayList<Law>());
            view.showDataEmptyMessage();
        } else {
            loadIfSearchTextExists();
        }
    }

    public void search(String searchText) {
        mSearchText = searchText;
        load();
    }

    public boolean isSortMenuVisible() {
        return !mSearchText.isEmpty();
    }

    public int getSortDialogType() {
        return DialogHelper.IDD_LAWS_SORT;
    }

    public List<Integer> getCurrentSortIndexValue(){
        List<Integer> result = new ArrayList<>();
        result.add(mSort);
        return result;
    }

    public void sort(List<Integer> oldSort, List<Integer> sort) {
        mSort = sort.get(0);
        mSortDirection = DatabaseHelper.getSortDirection(oldSort.get(0), mSort, mSortDirection);
        load();
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (LawsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    private void loadIfSearchTextExists(){
        view.showProgress();
        inter.loadLaws(mSearchText, mSortColumn.get(mSort) + mSortDirection, new LawsInteractor.LawsCallback() {
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
}
