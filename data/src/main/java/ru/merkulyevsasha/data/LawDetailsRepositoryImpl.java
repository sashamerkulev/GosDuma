package ru.merkulyevsasha.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository;
import ru.merkulyevsasha.gdcore.models.Codifier;
import ru.merkulyevsasha.gdcore.repositories.LawDetailsRepository;

public class LawDetailsRepositoryImpl implements LawDetailsRepository {

    private GDDatabaseRepository db;

    LawDetailsRepositoryImpl(GDDatabaseRepository db) {
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

    @Override
    public Single<Codifier> getStageById2(final int lastEventStageId) {
        return Single.fromCallable(new Callable<Codifier>() {
            @Override
            public Codifier call() throws Exception {
                return db.getStageById(lastEventStageId);
            }
        });
    }

    @Override
    public Single<Codifier> getPhaseById2(final int lastEventPhaseId) {
        return Single.fromCallable(new Callable<Codifier>() {
            @Override
            public Codifier call() throws Exception {
                return db.getPhaseById(lastEventPhaseId);
            }
        });
    }

    @Override
    public Single<List<Codifier>> getProfileComittees2(final int lawId) {
        return Single.fromCallable(new Callable<List<Codifier>>() {
            @Override
            public List<Codifier> call() throws Exception {
                return db.getProfileComittees(lawId);
            }
        });
    }

    @Override
    public Single<List<Codifier>> getCoexecutorCommittees2(final int lawId) {
        return Single.fromCallable(new Callable<List<Codifier>>() {
            @Override
            public List<Codifier> call() throws Exception {
                return db.getCoexecutorCommittees(lawId);
            }
        });
    }

    @Override
    public Single<List<Codifier>> getLawDeputies2(final int lawId) {
        return Single.fromCallable(new Callable<List<Codifier>>() {
            @Override
            public List<Codifier> call() throws Exception {
                return db.getLawDeputies(lawId);
            }
        });
    }

    @Override
    public Single<List<Codifier>> getLawFederals2(final int lawId) {
        return Single.fromCallable(new Callable<List<Codifier>>() {
            @Override
            public List<Codifier> call() throws Exception {
                return db.getLawFederals(lawId);
            }
        });
    }

    @Override
    public Single<List<Codifier>> getLawRegionals2(final int lawId) {
        return Single.fromCallable(new Callable<List<Codifier>>() {
            @Override
            public List<Codifier> call() throws Exception {
                return db.getLawRegionals(lawId);
            }
        });
    }
}
