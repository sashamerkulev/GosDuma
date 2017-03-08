package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;

public interface LawsInteractor {

    interface LawsCallback{

        void success(List<Law> items);
        void failure(Exception e);

    }

    void loadLaws(String searchText, String orderBy, LawsInteractor.LawsCallback callback);

}
