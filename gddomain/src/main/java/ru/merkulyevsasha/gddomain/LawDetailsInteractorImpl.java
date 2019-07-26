package ru.merkulyevsasha.gddomain;


import java.util.HashMap;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.LawDetailsRepository;


public class LawDetailsInteractorImpl implements LawDetailsInteractor {

    public final static String KEY_STAGE = "STAGE";
    public final static String KEY_PHASE = "PHASE";
    public final static String KEY_PROFILE = "PROFILE";
    public final static String KEY_COEXEC = "COEXEC";
    public final static String KEY_DEPUTIES = "DEPUTIES";
    public final static String KEY_DEPARTMENTS = "DEPARTMENTS";

    private final LawDetailsRepository repo;
    private final Scheduler scheduler;

    LawDetailsInteractorImpl(LawDetailsRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<HashMap<String, String>> getLawDetails(final Law law) {
        return Single.fromCallable(new Callable<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> call() throws Exception {

                HashMap<String, String> result = new HashMap<>();


                return result;
            }
        }).subscribeOn(scheduler);
    }
}
