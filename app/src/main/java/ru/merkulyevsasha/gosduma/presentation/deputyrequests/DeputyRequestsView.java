package ru.merkulyevsasha.gosduma.presentation.deputyrequests;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public interface DeputyRequestsView extends MvpView {

    interface OnDeputyRequestsClickListener {
        void onDeputyRequestwClick(DeputyRequest deputyRequest);
    }


    void showData(List<DeputyRequest> items);
    void showDataEmptyMessage();

}
