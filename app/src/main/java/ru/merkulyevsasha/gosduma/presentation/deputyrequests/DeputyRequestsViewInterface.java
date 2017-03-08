package ru.merkulyevsasha.gosduma.presentation.deputyrequests;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.DeputyRequest;


interface DeputyRequestsViewInterface {

    void show(List<DeputyRequest> items);

}
