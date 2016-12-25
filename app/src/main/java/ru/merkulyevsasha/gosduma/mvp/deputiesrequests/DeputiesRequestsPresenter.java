package ru.merkulyevsasha.gosduma.mvp.deputiesrequests;


import android.os.Bundle;

import java.util.List;

import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;

public class DeputiesRequestsPresenter implements PresenterInterface {




    @Override
    public int getSortDialogType() {
        return 0;
    }

    @Override
    public List<Integer> getCurrentSortIndexValue() {
        return null;
    }

    @Override
    public boolean isSortMenuVisible() {
        return false;
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
        return null;
    }

    @Override
    public void restoreState(Bundle outState) {

    }
}
