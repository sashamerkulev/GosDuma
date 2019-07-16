package ru.merkulyevsasha.gdcore.repositories;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.Codifier;

public interface LawDetailsRepository {

    Codifier getStageById(int lastEventStageId);
    Codifier getPhaseById(int lastEventPhaseId);

    List<Codifier> getProfileComittees(int lawId);
    List<Codifier> getCoexecutorCommittees(int lawId);

    List<Codifier> getLawDeputies(int lawId);

    List<Codifier> getLawFederals(int lawId);
    List<Codifier> getLawRegionals(int lawId);


    Single<Codifier> getStageById2(int lastEventStageId);
    Single<Codifier> getPhaseById2(int lastEventPhaseId);

    Single<List<Codifier>> getProfileComittees2(int lawId);
    Single<List<Codifier>> getCoexecutorCommittees2(int lawId);

    Single<List<Codifier>> getLawDeputies2(int lawId);

    Single<List<Codifier>> getLawFederals2(int lawId);
    Single<List<Codifier>> getLawRegionals2(int lawId);


}
