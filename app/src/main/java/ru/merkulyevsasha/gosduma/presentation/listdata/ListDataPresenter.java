package ru.merkulyevsasha.gosduma.presentation.listdata;

import java.util.List;

import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class ListDataPresenter implements MvpPresenter {

    private ListDataView view;
    private ListDataInteractor inter;

    public ListDataPresenter(ListDataInteractor inter) {
        this.inter = inter;
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (ListDataView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    public void load(String tableName){
        List<ListData> items = inter.select(tableName);
        view.showList(items);
    }
}
