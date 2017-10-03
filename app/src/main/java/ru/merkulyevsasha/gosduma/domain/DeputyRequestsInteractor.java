package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public interface DeputyRequestsInteractor {

    Single<List<DeputyRequest>> getDeputyRequests(final String searchText, final String orderBy);
}
