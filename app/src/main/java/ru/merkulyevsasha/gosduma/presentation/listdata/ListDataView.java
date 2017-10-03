package ru.merkulyevsasha.gosduma.presentation.listdata;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


interface ListDataView extends MvpView {

    void showList(List<ListData> items);

}
