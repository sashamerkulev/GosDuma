package ru.merkulyevsasha.gosduma.presentation;


import android.os.Bundle;

import java.util.List;

public interface MvpFragment {

    int getSortDialogType();
    int getCurrentSortIndexValue();
    boolean isSortMenuVisible();

    int getFilterDialogType();
    List<Integer> getCurrentFilterIndexValue();
    boolean isFilterMenuVisible();

    void search(String searchText);
    void sort(int oldSort, int sort);
    void filter(List<Integer> filter);

    Bundle getState();
    void restoreState(Bundle outState);

}
