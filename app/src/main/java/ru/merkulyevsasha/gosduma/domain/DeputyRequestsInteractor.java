package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public interface DeputyRequestsInteractor {

    interface DeputyRequestsCallback{

        void success(List<DeputyRequest> items);
        void failure(Exception e);

    }

    void loadDeputyRequests(final String searchText, final String orderBy, final DeputyRequestsInteractor.DeputyRequestsCallback callback);

}
