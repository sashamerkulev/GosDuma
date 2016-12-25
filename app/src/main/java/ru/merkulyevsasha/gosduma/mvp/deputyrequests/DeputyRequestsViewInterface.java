package ru.merkulyevsasha.gosduma.mvp.deputyrequests;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;


public interface DeputyRequestsViewInterface {

    void show(List<DeputyRequest> items);

}
