package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public interface DeputyRequestsRepository {

    Single<List<DeputyRequest>> getDeputyRequest2(String searchText, String orderBy);
}
