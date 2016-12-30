package ru.merkulyevsasha.gosduma.mvp.lawdetails;


import android.app.Activity;

import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.mvp.laws.BaseLawsPresenter;


class LawDetailsPresenter extends BaseLawsPresenter {

    public LawDetailsPresenter(Activity context) {
        super(context);
    }

    public Codifier getPhaseById(int id) {
        return DatabaseHelper.getInstance(mContext).getPhaseById(id);
    }

    public Codifier getStageById(int id) {
        return DatabaseHelper.getInstance(mContext).getStageById(id);
    }

    public List<Codifier> getProfileComittees(int id) {
        return DatabaseHelper.getInstance(mContext).getProfileComittees(id);
    }

    public List<Codifier> getCoexecutorCommittees(int id) {
        return DatabaseHelper.getInstance(mContext).getCoexecutorCommittees(id);
    }

    public List<Codifier> getLawDeputies(int id) {
        return DatabaseHelper.getInstance(mContext).getLawDeputies(id);
    }

    public List<Codifier> getLawRegionals(int id) {
        return DatabaseHelper.getInstance(mContext).getLawRegionals(id);
    }

    public List<Codifier> getLawFederals(int id) {
        return DatabaseHelper.getInstance(mContext).getLawFederals(id);
    }
}
