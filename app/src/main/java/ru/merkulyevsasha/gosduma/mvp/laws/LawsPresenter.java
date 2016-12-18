package ru.merkulyevsasha.gosduma.mvp.laws;


import android.content.Context;
import android.os.Bundle;

import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;


public class LawsPresenter implements PresenterInterface {

    private final Context mContext;
    private final ViewInterface mViewInterface;


    public LawsPresenter(Context context, ViewInterface viewInterface){

        mContext = context;
        mViewInterface = viewInterface;

    }

    @Override
    public int getSortDialogType() {
        return 0;
    }

    @Override
    public List<Integer> getCurrentSortIndexValue() {
        return null;
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
    public void search(String searchText) {

    }

    @Override
    public void sort(List<Integer> oldSort, List<Integer> sort) {

    }

    @Override
    public void filter(List<Integer> filter) {

    }

    public List<Law> getDeputyLaws(int deputyId){
        return DatabaseHelper.getInstance(mContext).getDeputyLaws(deputyId);
    }

    @Override
    public Bundle getState() {
        return null;
    }

    @Override
    public void restoreState(Bundle outState) {

    }
}
