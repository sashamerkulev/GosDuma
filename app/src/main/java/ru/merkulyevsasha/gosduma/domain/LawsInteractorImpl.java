package ru.merkulyevsasha.gosduma.domain;


import java.util.List;
import java.util.concurrent.ExecutorService;

import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.models.Law;

public class LawsInteractorImpl implements LawsInteractor {

    private LawsRepository repo;
    private ExecutorService executor;

    public LawsInteractorImpl(ExecutorService executor, LawsRepository repo){
        this.repo = repo;
        this.executor = executor;
    }

    @Override
    public void loadLaws(final String searchText, final String orderBy, final LawsCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Law> items = repo.getLaws(searchText, orderBy);
                    callback.success(items);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });
    }
}
