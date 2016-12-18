package ru.merkulyevsasha.gosduma.mvp;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;

public interface ViewInterface {

    interface OnClickListener {
        void onItemClick(int position);
    }

    interface OnDeputyClickListener{
        void onDeputyClick(Deputy deputy);
    }

    interface OnLawClickListener{
        void onLawClick(Law law);
    }


    interface OnViewListener {
        void onPresenterCreated(PresenterInterface presenter);
    }

    void show(List<Deputy> items);

}
