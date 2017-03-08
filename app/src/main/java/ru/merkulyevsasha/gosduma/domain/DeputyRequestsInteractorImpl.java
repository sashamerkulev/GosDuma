package ru.merkulyevsasha.gosduma.domain;


import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepository;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public class DeputyRequestsInteractorImpl implements DeputyRequestsInteractor {

    private DeputyRequestsRepository repo;
    private ExecutorService executor;

    public DeputyRequestsInteractorImpl(ExecutorService executor, DeputyRequestsRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void loadDeputyRequests(final String searchText, final String orderBy, final DeputyRequestsCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<DeputyRequest> items = repo.getDeputyRequest(searchText, orderBy);
                    callback.success(items);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }
}
