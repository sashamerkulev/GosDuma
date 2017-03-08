package ru.merkulyevsasha.gosduma.presentation.lawdetails;


import android.app.Activity;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.presentation.laws.BaseLawsPresenter;


class LawDetailsPresenter extends BaseLawsPresenter {

    public LawDetailsPresenter(Activity context) {
        super(context);
    }

    public Codifier getPhaseById(int id) {
        return mDatabase.getPhaseById(id);
    }

    public Codifier getStageById(int id) {
        return mDatabase.getStageById(id);
    }

    public List<Codifier> getProfileComittees(int id) {
        return mDatabase.getProfileComittees(id);
    }

    public List<Codifier> getCoexecutorCommittees(int id) {
        return mDatabase.getCoexecutorCommittees(id);
    }

    public List<Codifier> getLawDeputies(int id) {
        return mDatabase.getLawDeputies(id);
    }

    public List<Codifier> getLawRegionals(int id) {
        return mDatabase.getLawRegionals(id);
    }

    public List<Codifier> getLawFederals(int id) {
        return mDatabase.getLawFederals(id);
    }
}
