package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Law;

public interface DeputyDetailsInteractor {

    interface DeputyDetailsCallback{

        void success(List<Law> items);
        void failure(Exception e);

    }

    void loadDeputyLaws(final int deputyId, final String searchText, final String orderby, final DeputyDetailsInteractor.DeputyDetailsCallback callback);

}
