package ru.merkulyevsasha.gosduma.domain;


import java.util.HashMap;

import ru.merkulyevsasha.gosduma.models.Law;

public interface LawDetailsInteractor {

    interface LawDetailsCallback{

        void success(HashMap<String, String> result);
        void failure(Exception e);

    }

    void loadLawDetails(Law law, LawDetailsInteractor.LawDetailsCallback callback);

}
