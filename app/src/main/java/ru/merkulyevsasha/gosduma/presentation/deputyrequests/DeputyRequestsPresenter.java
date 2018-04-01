package ru.merkulyevsasha.gosduma.presentation.deputyrequests;




import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class DeputyRequestsPresenter extends MvpPresenter<DeputyRequestsView> {

    private final static int NAME_INDEX = 0;
    private final static int REQUESTDATE_INDEX = 1;
    private final static int INITIATOR_INDEX = 2;

    private final DeputyRequestsInteractor inter;

    private final HashMap<Integer, String> mSortColumn;

    private int mSort;
    private String mSortDirection;
    private String mSearchText;

    @SuppressLint("UseSparseArrays")
    @Inject
    public DeputyRequestsPresenter(DeputyRequestsInteractor inter){
        super();

        this.inter = inter;

        mSort = NAME_INDEX;
        mSortDirection = DatabaseHelper.ASC;

        mSearchText = "";

        mSortColumn = new HashMap<>();
        mSortColumn.put(NAME_INDEX, "name");
        mSortColumn.put(REQUESTDATE_INDEX, "requestDate");
        mSortColumn.put(INITIATOR_INDEX, "initiator");
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

    public void load(){
        if (view == null) return;
        view.showProgress();
        compositeDisposable.add(inter.getDeputyRequests(mSearchText, mSortColumn.get(mSort) + mSortDirection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<DeputyRequest>>() {
                    @Override
                    public void accept(@NonNull List<DeputyRequest> deputyRequests) throws Exception {
                        if (view == null) return;
                        view.hideProgress();
                        if (deputyRequests.size() > 0) view.showData(deputyRequests);
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
                }));
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

    void onDeputyRequestClicked(boolean activity, DeputyRequest deputyRequest) {
        if (view == null) return;
        if (activity) view.showDeputyRequestDetailsScreen(deputyRequest);
        else view.showDeputyRequestDetailsFragment(deputyRequest);
    }
}
