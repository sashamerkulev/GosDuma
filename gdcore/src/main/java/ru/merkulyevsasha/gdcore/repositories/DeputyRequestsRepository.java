package ru.merkulyevsasha.gdcore.repositories;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.DeputyRequest;


public interface DeputyRequestsRepository {

    Single<List<DeputyRequest>> getDeputyRequest2(String searchText, String orderBy);
}
