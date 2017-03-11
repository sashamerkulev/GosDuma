package ru.merkulyevsasha.gosduma.presentation.deputies;


import android.content.Context;
import android.os.Bundle;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.helpers.DialogHelper;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class DeputiesPresenter implements MvpPresenter {

    private final static int NAME_INDEX = 0;
    private final static int BIRTHDATE_INDEX = 1;
    private final static int FRACTIONNAME_INDEX = 2;

    public final static int DEPUTY_INDEX = 0;
    public final static int MEMBER_INDEX = 1;
    public final static int WORKING_INDEX = 1;
    public final static int NOT_WORKING_INDEX = 0;

    private DeputiesView view;

    private final HashMap<Integer, String> mSortColumn;
    private final HashMap<Integer, String> mFilterDeputyValues;

    private int mSort;
    private String mSortDirection;
    private int mFilterDeputy;
    private int mFilterWorking;
    private String mSearchText;

    private DeputiesInteractor inter;

    public DeputiesPresenter(Context context, DeputiesInteractor inter){

        this.inter = inter;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mFilterDeputy = DEPUTY_INDEX;
        mFilterWorking = WORKING_INDEX;

        mSearchText = "";

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "d.name");
        mSortColumn.put(BIRTHDATE_INDEX, "di.birthdate");
        mSortColumn.put(FRACTIONNAME_INDEX, "di.factionName");

        mFilterDeputyValues= new HashMap<>();
        mFilterDeputyValues.put(DEPUTY_INDEX, context.getResources().getString(R.string.text_deputy));
        mFilterDeputyValues.put(MEMBER_INDEX, context.getResources().getString(R.string.text_member_sf));
    }

    public int getSortDialogType() {
        return DialogHelper.IDD_DEPUTY_SORT;
    }

    public int getFilterDialogType() {
        return DialogHelper.IDD_DEPUTY_FILTER;
    }

    public int getCurrentSortIndexValue(){
        return mSort;
    }

    public boolean isSortMenuVisible() {
        return true;
    }

    public List<Integer> getCurrentFilterIndexValue(){
        List<Integer> result = new ArrayList<>();
        result.add(mFilterDeputy);
        result.add(mFilterWorking);
        return result;
    }

    public boolean isFilterMenuVisible() {
        return true;
    }

    public void search(String searchText) {
        mSearchText = searchText;
        load();
    }

    public void sort(int oldSort, int sort){
        mSort = sort;
        mSortDirection = DatabaseHelper.getSortDirection(oldSort, mSort, mSortDirection);
        load();
    }

    public void filter(List<Integer> filter){
        mFilterDeputy = filter.get(0);
        mFilterWorking = filter.get(1);
        load();
    }

    public void load(){
        inter.loadDeputies(mSearchText, mSortColumn.get(mSort) + mSortDirection,
                mFilterDeputyValues.get(mFilterDeputy), mFilterWorking, new DeputiesInteractor.DeputiesCallback() {
                    @Override
                    public void success(List<Deputy> items) {
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
                    }
                });
    }

    public Bundle getState(){
        Bundle state = new Bundle();

        state.putInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putInt(KeysBundleHolder.KEY_CURRENT_FILTERDEPUTY_VALUE, mFilterDeputy);
        state.putInt(KeysBundleHolder.KEY_CURRENT_FILTERWORKING_VALUE, mFilterWorking);
        state.putString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    public void restoreState(Bundle outState){

        if (outState != null){
            mSort = outState.getInt(KeysBundleHolder.KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KeysBundleHolder.KEY_CURRENT_SORT_DIRECTIONVALUE);
            mFilterDeputy = outState.getInt(KeysBundleHolder.KEY_CURRENT_FILTERDEPUTY_VALUE);
            mFilterWorking = outState.getInt(KeysBundleHolder.KEY_CURRENT_FILTERWORKING_VALUE);
            mSearchText = outState.getString(KeysBundleHolder.KEY_CURRENT_SEARCHTEXT_VALUE);
        }
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (DeputiesView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }
}
