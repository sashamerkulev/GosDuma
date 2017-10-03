package ru.merkulyevsasha.gosduma.presentation.deputyrequests;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


interface DeputyRequestsView extends MvpView {

    void showSortDialog(int currentItemIndex);

    void showData(List<DeputyRequest> items);
    void showDataEmptyMessage();

    void showDeputyRequestDetailsScreen(DeputyRequest deputyRequest);

    void prepareToSearch(String searchText);
}
