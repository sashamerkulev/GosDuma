package ru.merkulyevsasha.gosduma.presentation.deputies;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

interface DeputiesView extends MvpView {

    void prepareToSearch(String searchText);

    void showData(List<Deputy> items);
    void showDataEmptyMessage();

    void showDeputyDetailsScreen(Deputy deputy);

    void showFilterDialog(List<Integer> filter);
    void showSortDialog(int currentItemIndex);

}
