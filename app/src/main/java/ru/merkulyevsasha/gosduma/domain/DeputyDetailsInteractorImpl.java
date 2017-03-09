package ru.merkulyevsasha.gosduma.domain;


import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepository;
import ru.merkulyevsasha.gosduma.models.Law;

public class DeputyDetailsInteractorImpl implements DeputyDetailsInteractor{

    private DeputyDetailsRepository repo;
    private ExecutorService executor;

    public DeputyDetailsInteractorImpl(ExecutorService executor, DeputyDetailsRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void loadDeputyLaws(final int deputyId, final String searchText, final String orderby, final DeputyDetailsInteractor.DeputyDetailsCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Law> items = repo.getDeputyLaws(deputyId, searchText, orderby);
                    callback.success(items);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }


}
