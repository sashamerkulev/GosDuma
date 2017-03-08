package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public interface DeputyRequestsRepository {

    List<DeputyRequest> getDeputyRequest(String searchText, String orderBy);

}
