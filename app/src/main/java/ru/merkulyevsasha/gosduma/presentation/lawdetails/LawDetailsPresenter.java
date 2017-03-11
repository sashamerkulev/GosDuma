package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import com.google.firebase.crash.FirebaseCrash;

import java.util.HashMap;

import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class LawDetailsPresenter implements MvpPresenter {

    private LawDetailsView view;
    private LawDetailsInteractor inter;

    public LawDetailsPresenter(LawDetailsInteractor inter) {
        this.inter = inter;
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (LawDetailsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    public void load(Law law){
        view.showProgress();
        inter.loadLawDetails(law, new LawDetailsInteractor.LawDetailsCallback() {
            @Override
            public void success(HashMap<String, String> result) {
                if (view == null)
                    return;

                view.hideProgress();
                view.showData(result);
            }

            @Override
            public void failure(Exception e) {
                FirebaseCrash.report(e);

                if (view == null)
                    return;

                view.hideProgress();
                view.showEmptyData();
            }
        });
    }

}
