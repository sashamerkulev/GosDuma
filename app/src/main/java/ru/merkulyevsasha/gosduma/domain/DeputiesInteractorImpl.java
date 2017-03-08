package ru.merkulyevsasha.gosduma.domain;


import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesInteractorImpl implements DeputiesInteractor {

    private DeputiesRepository repo;
    private ExecutorService executor;

    public DeputiesInteractorImpl(ExecutorService executor, DeputiesRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void loadDeputies(final String searchText, final String orderBy, final String position, final int isCurrent, final DeputiesCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Deputy> items = repo.getDeputies(searchText, orderBy, position, isCurrent);
                    callback.success(items);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }
}
