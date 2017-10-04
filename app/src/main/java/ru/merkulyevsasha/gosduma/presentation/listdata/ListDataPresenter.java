package ru.merkulyevsasha.gosduma.presentation.listdata;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class ListDataPresenter extends MvpPresenter<ListDataView> {

    private final ListDataInteractor inter;

    @Inject
    public ListDataPresenter(ListDataInteractor inter) {
        this.inter = inter;
    }

    public void load(String tableName){
        if (view == null) return;
        view.showProgress();
        inter.getList(tableName).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ListData>>() {
                    @Override
                    public void accept(@NonNull List<ListData> listDatas) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        view.showList(listDatas);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        view.showMessage(R.string.error_loading_news_message);
                    }
                });


    }
}
