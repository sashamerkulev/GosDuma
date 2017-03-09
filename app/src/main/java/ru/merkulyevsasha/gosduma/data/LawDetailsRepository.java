package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Codifier;

public interface LawDetailsRepository {

    Codifier getStageById(int lastEventStageId);
    Codifier getPhaseById(int lastEventPhaseId);

    List<Codifier> getProfileComittees(int lawId);
    List<Codifier> getCoexecutorCommittees(int lawId);

    List<Codifier> getLawDeputies(int lawId);

    List<Codifier> getLawFederals(int lawId);
    List<Codifier> getLawRegionals(int lawId);

}
