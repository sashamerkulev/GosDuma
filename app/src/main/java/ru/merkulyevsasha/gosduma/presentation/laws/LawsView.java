package ru.merkulyevsasha.gosduma.presentation.laws;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpView;

public interface LawsView extends MvpView{

    interface OnLawClickListener{
        void onLawClick(Law law);
    }

    void showData(List<Law> items);
    void showDataEmptyMessage();

}
