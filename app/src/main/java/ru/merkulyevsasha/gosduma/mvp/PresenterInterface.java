package ru.merkulyevsasha.gosduma.mvp;


import android.os.Bundle;

import java.util.List;

public interface PresenterInterface {

    int getSortDialogType();
    List<Integer> getCurrentSortIndexValue();

    int getFilterDialogType();
    List<Integer> getCurrentFilterIndexValue();

    void search(String searchText);
    void sort(List<Integer> oldSort, List<Integer> sort);
    void filter(List<Integer> filter);

    Bundle getState();
    void restoreState(Bundle outState);

}
