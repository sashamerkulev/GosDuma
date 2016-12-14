package ru.merkulyevsasha.gosduma.mvp;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;

public interface ViewInterface {

    interface OnClickListener {
        void onItemClick(int position);
    }

    interface onDeputyClickListener{
        void onDeputyClick(Deputy deputy);
    }

    interface OnViewListener {
        void onPresenterCreated(PresenterInterface presenter);
    }

    void show(List<Deputy> items);

}
