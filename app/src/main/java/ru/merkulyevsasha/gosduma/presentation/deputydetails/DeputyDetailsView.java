package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

interface DeputyDetailsView extends MvpView {

    void showData(List<Law> items);
    void showDataEmptyMessage();

    void showLawDetailsScreen(Law law);

    void share(Deputy deputy);

    void showSortDialog(int currentItemIndex);
}
