package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Codifier;

public class LawDetailsRepositoryImpl implements LawDetailsRepository {

    private DatabaseHelper db;

    public LawDetailsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }


    @Override
    public Codifier getStageById(int lastEventStageId) {
        return db.getStageById(lastEventStageId);
    }

    @Override
    public Codifier getPhaseById(int lastEventPhaseId) {
        return db.getPhaseById(lastEventPhaseId);
    }

    @Override
    public List<Codifier> getProfileComittees(int lawId) {
        return db.getProfileComittees(lawId);
    }

    @Override
    public List<Codifier> getCoexecutorCommittees(int lawId) {
        return db.getCoexecutorCommittees(lawId);
    }

    @Override
    public List<Codifier> getLawDeputies(int lawId) {
        return db.getLawDeputies(lawId);
    }

    @Override
    public List<Codifier> getLawFederals(int lawId) {
        return db.getLawFederals(lawId);
    }

    @Override
    public List<Codifier> getLawRegionals(int lawId) {
        return db.getLawRegionals(lawId);
    }
}
