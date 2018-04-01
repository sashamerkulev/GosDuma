package ru.merkulyevsasha.gosduma.presentation.deputydetails;




import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class DeputyDetailsPresenter extends MvpPresenter<DeputyDetailsView> {

    private final static int NAME_INDEX = 0;
    private final static int NUMBER_INDEX = 1;
    private final static int DATE_INDEX = 2;

    private int mDeputyId;
    private HashMap<Integer, String> mSortColumn;

    private int mSort;
    private String mSortDirection;
    private String mSearchText;

    private final DeputyDetailsInteractor inter;

    @SuppressLint("UseSparseArrays")
    @Inject
    public DeputyDetailsPresenter(DeputyDetailsInteractor inter){

        this.inter = inter;

        mSearchText = "";

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "l.name");
        mSortColumn.put(NUMBER_INDEX, "l.number");
        mSortColumn.put(DATE_INDEX, "l.introductionDate");
    }

    void onSearchTextSubmitted(String searchText) {
        mSearchText = searchText;
        load(mDeputyId);
    }

    void sort(int oldSort, int sort) {
        mSort = sort;
        mSortDirection = DatabaseHelper.getSortDirection(oldSort, mSort, mSortDirection);
        load(mDeputyId);
    }

    void load(int deputyId){
        mDeputyId = deputyId;
        view.showProgress();
        compositeDisposable.add(inter.getDeputyLaws(deputyId, mSearchText, mSortColumn.get(mSort) + mSortDirection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Law>>() {
                    @Override
                    public void accept(@NonNull List<Law> laws) {
                        if (view == null) return;
                        view.hideProgress();
                        if (laws.size() > 0) view.showData(laws);
                        else view.showDataEmptyMessage();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) {
                        if (view == null) return;
                        view.hideProgress();
                        view.showMessage(R.string.error_loading_news_message);
                    }
                }));
    }

    void onLawClicked(Law law) {
        if (view == null) return;
        view.showLawDetailsScreen(law);
    }

    void onSearchTextChanged(int deputyId, String newText) {
        if (newText == null || newText.isEmpty()){
            mSearchText = newText;
            load(deputyId);
        }
    }

    void onSortItemClicked() {
        if (view == null) return;
        view.showSortDialog(mSort);
    }
}
