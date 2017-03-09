package ru.merkulyevsasha.gosduma.domain;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;


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


    private LawDetailsRepository repo;
    private ExecutorService executor;

    public LawDetailsInteractorImpl(ExecutorService executor, LawDetailsRepository repo){
        this.repo = repo;
        this.executor = executor;
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
    public void loadLawDetails(final Law law, final LawDetailsCallback callback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {

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

                    callback.success(result);
                } catch(Exception e){
                    callback.failure(e);
                }
            }
        });

    }
}
