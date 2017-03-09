package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

public interface DeputyDetailsView extends MvpView {

    void showData(List<Law> items);
    void showDataEmptyMessage();


}
