package ru.merkulyevsasha.gosduma.domain;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;


import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepository;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Law;


public class LawDetailsInteractorImpl implements LawDetailsInteractor {

    public final static String KEY_STAGE = "STAGE";
    public final static String KEY_PHASE = "PHASE";
    public final static String KEY_PROFILE = "PROFILE";
    public final static String KEY_COEXEC = "COEXEC";
    public final static String KEY_DEPUTIES = "DEPUTIES";
    public final static String KEY_DEPARTMENTS = "DEPARTMENTS";

    private final LawDetailsRepository repo;
    private final Scheduler scheduler;

    public LawDetailsInteractorImpl(LawDetailsRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    private String joinCodifiers(List<Codifier> list){
        StringBuilder sb = new StringBuilder();

        for (Codifier item : list) {
            if (sb.length() > 0){
                sb.append(", ");
            }
            sb.append(item.name);
        }

        return sb.toString();
    }

    @Override
    public Single<HashMap<String, String>> getLawDetails(final Law law) {
        return Single.fromCallable(new Callable<HashMap<String, String>>() {
            @Override
            public HashMap<String, String> call() throws Exception {

                HashMap<String, String> result = new HashMap<>();

                Codifier stage = repo.getStageById(law.lastEventStageId);
                result.put(KEY_STAGE, stage.name);
                Codifier phase = repo.getPhaseById(law.lastEventPhaseId);
                result.put(KEY_PHASE, phase.name);

                List<Codifier> profiles = repo.getProfileComittees(law.id);
                String sprofiles = joinCodifiers(profiles);
                result.put(KEY_PROFILE, sprofiles);

                List<Codifier> coexecutors = repo.getCoexecutorCommittees(law.id);
                String scoexecutors = joinCodifiers(coexecutors);
                result.put(KEY_COEXEC, scoexecutors);

                List<Codifier> deputies = repo.getLawDeputies(law.id);
                String sdeputies = joinCodifiers(deputies);
                result.put(KEY_DEPUTIES, sdeputies);

                List<Codifier> federals = repo.getLawFederals(law.id);
                List<Codifier> regionals = repo.getLawRegionals(law.id);
                federals.addAll(regionals);
                String departments = joinCodifiers(federals);
                result.put(KEY_DEPARTMENTS, departments);

                return result;
            }
        }).subscribeOn(scheduler);
    }
}
