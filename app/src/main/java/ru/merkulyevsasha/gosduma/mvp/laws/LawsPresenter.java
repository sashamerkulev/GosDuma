package ru.merkulyevsasha.gosduma.mvp.laws;


import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.DialogHelper;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;


public class LawsPresenter implements PresenterInterface {

    public final static int NAME_INDEX = 0;
    public final static int NUMBER_INDEX = 1;
    public final static int DATE_INDEX = 2;

    private final static String KEY_CURRENT_SORT_VALUE = "SORT";
    private final static String KEY_CURRENT_SORT_DIRECTIONVALUE = "SORT_DIRECTION";
    private final static String KEY_DEPUTY_ID = "DEPUTY_ID";

    private final Context mContext;
    private final LawsViewInterface mViewInterface;
    private final HashMap<Integer, String> mSortColumn;

    private int mDeputyId;
    private int mSort;
    private String mSortDirection;
    private String mSearchText;

    public LawsPresenter(Context context, LawsViewInterface viewInterface){

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
    public int getFilterDialogType() {
        return 0;
    }

    @Override
    public List<Integer> getCurrentFilterIndexValue() {
        return null;
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
    public void filter(List<Integer> filter) {}

    @Override
    public Bundle getState() {
        Bundle state = new Bundle();

        state.putInt(KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putInt(KEY_DEPUTY_ID, mDeputyId);

        return state;
    }

    @Override
    public void restoreState(Bundle outState) {
        if (outState != null){
            mSort = outState.getInt(KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KEY_CURRENT_SORT_DIRECTIONVALUE);
            mDeputyId = outState.getInt(KEY_DEPUTY_ID);
        }
    }

    public Codifier getPhaseById(int id) {
        return DatabaseHelper.getInstance(mContext).getPhaseById(id);
    }

    public Codifier getStageById(int id) {
        return DatabaseHelper.getInstance(mContext).getStageById(id);
    }

    public List<Codifier> getProfileComittees(int id) {
        return DatabaseHelper.getInstance(mContext).getProfileComittees(id);
    }

    public List<Codifier> getCoexecutorCommittees(int id) {
        return DatabaseHelper.getInstance(mContext).getCoexecutorCommittees(id);
    }

    public List<Codifier> getLawDeputies(int id) {
        return DatabaseHelper.getInstance(mContext).getLawDeputies(id);
    }

    public List<Codifier> getLawRegionals(int id) {
        return DatabaseHelper.getInstance(mContext).getLawRegionals(id);
    }

    public List<Codifier> getLawFederals(int id) {
        return DatabaseHelper.getInstance(mContext).getLawFederals(id);
    }

}
