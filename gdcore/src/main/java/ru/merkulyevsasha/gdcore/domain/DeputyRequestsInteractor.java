package ru.merkulyevsasha.gdcore.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.DeputyRequest;

public interface DeputyRequestsInteractor {

    Single<List<DeputyRequest>> getDeputyRequests(final String searchText, final String orderBy);
}
