package ru.merkulyevsasha.gosduma.presentation.deputies;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

public interface DeputiesView extends MvpView {

    interface OnDeputyClickListener{
        void onDeputyClick(Deputy deputy);
    }

    void showData(List<Deputy> items);
    void showDataEmptyMessage();


}
