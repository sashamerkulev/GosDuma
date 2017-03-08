package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Deputy;

public interface DeputiesInteractor {

    interface DeputiesCallback{

        void success(List<Deputy> items);
        void failure(Exception e);

    }

    void loadDeputies(String searchText, String orderBy, String position, int isCurrent, DeputiesInteractor.DeputiesCallback callback);


}
