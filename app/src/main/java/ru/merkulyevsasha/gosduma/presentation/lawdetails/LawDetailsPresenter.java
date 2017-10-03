package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import com.google.firebase.crash.FirebaseCrash;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gosduma.models.Deputy;
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

    public void load(Law law) {
        if (view == null) return;
        view.showProgress();
        inter.getLawDetails(law)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HashMap<String, String>>() {
                    @Override
                    public void accept(@NonNull HashMap<String, String> lawDetails) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        view.showData(lawDetails);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        view.showMessage(R.string.error_loading_news_message);
                        view.showEmptyData();
                    }
                });
    }

    void onSharedClicked(Law law) {
        if (view == null) return;
        view.share(law);
    }
}
