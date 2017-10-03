package ru.merkulyevsasha.gosduma.presentation.laws;



import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class LawsPresenter implements MvpPresenter{

    final static int NAME_INDEX = 0;
    final static int NUMBER_INDEX = 1;
    final static int DATE_INDEX = 2;

    private int mSort;
    private String mSortDirection;
    private String mSearchText;
    private HashMap<Integer, String> mSortColumn;

    private LawsView view;
    private LawsInteractor inter;

    public LawsPresenter(LawsInteractor inter) {

        this.inter = inter;

        mSearchText = "";

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(NUMBER_INDEX, "number");
        mSortColumn.put(DATE_INDEX, "introductionDate");
    }

    public void load(){
        if (mSearchText.isEmpty()){
            view.showDataEmptyMessage();
        } else {
            loadIfSearchTextExists();
        }
    }

    private void search(String searchText) {
        mSearchText = searchText;
        load();
    }

    public void sort(int oldSort, int sort) {
        mSort = sort;
        mSortDirection = DatabaseHelper.getSortDirection(oldSort, mSort, mSortDirection);
        load();
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (LawsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    private void loadIfSearchTextExists(){
        if (view==null) return;
        view.showProgress();
        inter.getLaws(mSearchText, mSortColumn.get(mSort) + mSortDirection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Law>>() {
                    @Override
                    public void accept(@NonNull List<Law> laws) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        if (laws.size() > 0) view.showData(laws);
                        else view.showDataEmptyMessage();
                        view.prepareToSearch(mSearchText);
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

    void onSortItemClicked() {
        if (view == null) return;
        view.showSortDialog(mSort);
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
        if (newText == null || newText.isEmpty()){
            mSearchText = newText;
            load();
        }
    }

    void onLawClicked(Law law) {
        if (view == null) return;
        view.showLawDetailsScreen(law);
    }

}
