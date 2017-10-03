package ru.merkulyevsasha.gosduma.presentation.laws;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

public interface LawsView extends MvpView{

    void prepareToSearch(String searchText);

    void showData(List<Law> items);
    void showDataEmptyMessage();

    void showSortDialog(int currentItemIndex);

    void showLawDetailsScreen(Law law);
}
