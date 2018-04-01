package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class LawDetailsPresenter extends MvpPresenter<LawDetailsView> {

    private final LawDetailsInteractor inter;

    @Inject
    public LawDetailsPresenter(LawDetailsInteractor inter) {
        super();
        this.inter = inter;
    }

    public void load(Law law) {
        if (view == null) return;
        view.showProgress();
        compositeDisposable.add(inter.getLawDetails(law)
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
                }));
    }

}
