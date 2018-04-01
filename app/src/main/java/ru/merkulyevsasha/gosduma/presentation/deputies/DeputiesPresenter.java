package ru.merkulyevsasha.gosduma.presentation.deputies;


import android.annotation.SuppressLint;
import android.content.Context;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class DeputiesPresenter extends MvpPresenter<DeputiesView> {

    private final static int NAME_INDEX = 0;
    private final static int BIRTHDATE_INDEX = 1;
    private final static int FRACTIONNAME_INDEX = 2;

    public final static int DEPUTY_INDEX = 0;
    public final static int MEMBER_INDEX = 1;
    public final static int WORKING_INDEX = 1;
    public final static int NOT_WORKING_INDEX = 0;

    private final HashMap<Integer, String> mSortColumn;
    private final HashMap<Integer, String> mFilterDeputyValues;

    private int mSort;
    private String mSortDirection;
    private int mFilterDeputy;
    private int mFilterWorking;
    private String mSearchText;

    private final DeputiesInteractor inter;

    @SuppressLint("UseSparseArrays")
    @Inject
    public DeputiesPresenter(Context context, DeputiesInteractor inter){

        this.inter = inter;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mFilterDeputy = DEPUTY_INDEX;
        mFilterWorking = WORKING_INDEX;

        mSearchText = "";

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "d.name");
        mSortColumn.put(BIRTHDATE_INDEX, "di.birthdate");
        mSortColumn.put(FRACTIONNAME_INDEX, "di.factionName");

        mFilterDeputyValues= new HashMap<>();
        mFilterDeputyValues.put(DEPUTY_INDEX, context.getResources().getString(R.string.text_deputy));
        mFilterDeputyValues.put(MEMBER_INDEX, context.getResources().getString(R.string.text_member_sf));
    }

    private List<Integer> getCurrentFilterIndexValue(){
        List<Integer> result = new ArrayList<>();
        result.add(mFilterDeputy);
        result.add(mFilterWorking);
        return result;
    }

    private void search(String searchText) {
        mSearchText = searchText;
        load();
    }

    void sort(int oldSort, int sort){
        mSort = sort;
        mSortDirection = DatabaseHelper.getSortDirection(oldSort, mSort, mSortDirection);
        load();
    }

    void filter(List<Integer> filter){
        mFilterDeputy = filter.get(0);
        mFilterWorking = filter.get(1);
        load();
    }

    void load(){
        if (view ==null) return;
        view.showProgress();
        compositeDisposable.add(inter.getDeputies(mSearchText, mSortColumn.get(mSort) + mSortDirection, mFilterDeputyValues.get(mFilterDeputy), mFilterWorking)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Deputy>>() {
                    @Override
                    public void accept(@NonNull List<Deputy> deputies) {
                        if (view == null) return;
                        view.hideProgress();
                        if (deputies.size() > 0) view.showData(deputies);
                        else view.showDataEmptyMessage();
                        view.prepareToSearch(mSearchText);
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

    void onDeputyClicked(boolean frameDetailsExists, Deputy deputy) {
        if (view == null) return;
        if (frameDetailsExists) view.showDeputyDetailsFragment(deputy);
        else view.showDeputyDetailsScreen(deputy);
    }

    void onSortItemClicked() {
        if (view == null) return;
        view.showSortDialog(mSort);
    }

    void onFilterItemClicked() {
        if (view == null) return;
        view.showFilterDialog(getCurrentFilterIndexValue());
    }

    void onSearchTextSubmitted(String query) {
        if (view == null) return;
        if (query == null || query.isEmpty()) load();
        else {
            if (query.length() < 3) view.showMessage(R.string.search_lvalidation_message);
            else search(query);
        }
    }

    void onSearchTextChanged(String newText) {
        if ((newText == null || newText.isEmpty()) && !mSearchText.isEmpty()){
            mSearchText = newText;
            load();
        }
    }
}
