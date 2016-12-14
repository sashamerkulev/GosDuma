package ru.merkulyevsasha.gosduma.mvp;


import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.MainActivity;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesPresenter implements PresenterInterface{

    public final static int NAME_INDEX = 0;
    public final static int BIRTHDATE_INDEX = 1;
    public final static int FRACTIONNAME_INDEX = 2;

    public final static int DEPUTY_INDEX = 0;
    public final static int MEMBER_INDEX = 1;
    public final static int WORKING_INDEX = 1;
    public final static int NOT_WORKING_INDEX = 0;

    private final static String KEY_CURRENT_SORT_VALUE = "SORT";
    private final static String KEY_CURRENT_SORT_DIRECTIONVALUE = "SORT_DIRECTION";
    private final static String KEY_CURRENT_FILTERDEPUTY_VALUE = "DEPUTY";
    private final static String KEY_CURRENT_FILTERWORKING_VALUE = "WORKING";
    private final static String KEY_CURRENT_SEARCHTEXT_VALUE = "SEARCH";

    private final Context mContext;
    private final ViewInterface mViewInterface;
    private final HashMap<Integer, String> mSortColumn;
    private final HashMap<Integer, String> mFilterDeputyValues;

    private int mSort;
    private String mSortDirection;
    private int mFilterDeputy;
    private int mFilterWorking;
    private String mSearchText;

    public DeputiesPresenter(Context context, ViewInterface viewInterface){

        mContext = context;
        mViewInterface = viewInterface;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mFilterDeputy = DEPUTY_INDEX;
        mFilterWorking = WORKING_INDEX;

        mSearchText = "";

        mSortColumn = new HashMap<Integer, String>();
        mSortColumn.put(NAME_INDEX, "d.name");
        mSortColumn.put(BIRTHDATE_INDEX, "di.birthdate");
        mSortColumn.put(FRACTIONNAME_INDEX, "di.factionName");

        mFilterDeputyValues= new HashMap<Integer, String>();
        mFilterDeputyValues.put(DEPUTY_INDEX, "Депутат ГД");
        mFilterDeputyValues.put(MEMBER_INDEX, "Член СФ");
    }

    @Override
    public int getSortDialogType() {
        return MainActivity.IDD_DEPUTY_SORT;
    }

    @Override
    public int getFilterDialogType() {
        return MainActivity.IDD_DEPUTY_FILTER;
    }

    @Override
    public List<Integer> getCurrentSortIndexValue(){
        List<Integer> result = new ArrayList<Integer>();
        result.add(mSort);
        return result;
    }

    @Override
    public List<Integer> getCurrentFilterIndexValue(){
        List<Integer> result = new ArrayList<Integer>();
        result.add(mFilterDeputy);
        result.add(mFilterWorking);
        return result;
    }

    public List<Deputy> getDeputies(){
        return DatabaseHelper.getInstance(mContext).search(mSearchText, mSortColumn.get(mSort) + mSortDirection,
                mFilterDeputyValues.get(mFilterDeputy), mFilterWorking);
    }

    @Override
    public void search(String searchText) {
        mSearchText = searchText;
        mViewInterface.show(getDeputies());
    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort){
        mSort = sort.get(0);
        if (oldSort.get(0).equals(sort.get(0))){
            if (mSortDirection.trim().equals(DatabaseHelper.DESC.trim())){
                mSortDirection = DatabaseHelper.ASC;
            } else {
                mSortDirection = DatabaseHelper.DESC;
            }
        } else {
            mSortDirection = DatabaseHelper.ASC;
        }
        mViewInterface.show(getDeputies());
    }

    @Override
    public void filter(List<Integer> filter){
        mFilterDeputy = filter.get(0);
        mFilterWorking = filter.get(1);
        mViewInterface.show(getDeputies());
    }

    @Override
    public Bundle getState(){
        Bundle state = new Bundle();

        state.putInt(KEY_CURRENT_SORT_VALUE, mSort);
        state.putString(KEY_CURRENT_SORT_DIRECTIONVALUE, mSortDirection);
        state.putInt(KEY_CURRENT_FILTERDEPUTY_VALUE, mFilterDeputy);
        state.putInt(KEY_CURRENT_FILTERWORKING_VALUE, mFilterWorking);
        state.putString(KEY_CURRENT_SEARCHTEXT_VALUE, mSearchText);

        return state;
    }

    @Override
    public void restoreState(Bundle outState){

        if (outState != null){
            mSort = outState.getInt(KEY_CURRENT_SORT_VALUE);
            mSortDirection = outState.getString(KEY_CURRENT_SORT_DIRECTIONVALUE);
            mFilterDeputy = outState.getInt(KEY_CURRENT_FILTERDEPUTY_VALUE);
            mFilterWorking = outState.getInt(KEY_CURRENT_FILTERWORKING_VALUE);
            mSearchText = outState.getString(KEY_CURRENT_SEARCHTEXT_VALUE);
        }
    }

}