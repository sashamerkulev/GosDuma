package ru.merkulyevsasha.gosduma.mvp.deputiesrequests;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;


public interface DeputiesRequestsViewInterface {

    void show(List<DeputyRequest> items);

}
