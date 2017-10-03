package ru.merkulyevsasha.gosduma.presentation.laws;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.DrawerToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.commons.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;


public class LawsFragment extends Fragment implements LawsView {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.layout_empty) LinearLayout mEmptyLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.adView) AdView mAdView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @Inject LawsPresenter pres;

    private View rootView;
    private DrawerToolbarCombinator combinator;

    private LawsRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private MenuItem sortItem;
    private MenuItem searchItem;
    private SearchView searchView;
    private String searchText;

    private AppbarScrollExpander appbarScrollExpander;
    private int position;
    private boolean expanded;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DrawerToolbarCombinator) {
            combinator = (DrawerToolbarCombinator) context;
        }
        GosDumaApp.getComponent().inject(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(KeysBundleHolder.KEY_POSITION, layoutManager.findFirstVisibleItemPosition());
        outState.putBoolean(KeysBundleHolder.KEY_EXPANDED, appbarScrollExpander.getExpanded());
        outState.putString(KeysBundleHolder.KEY_SEARCHTEXT, searchText);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null){
            position = savedInstanceState.getInt(KeysBundleHolder.KEY_POSITION, -1);
            expanded = savedInstanceState.getBoolean(KeysBundleHolder.KEY_EXPANDED, true);
            searchText = savedInstanceState.getString(KeysBundleHolder.KEY_SEARCHTEXT, "");
        } else {
            expanded = true;
            position = -1;
            searchText = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_laws, container, false);
        ButterKnife.bind(this, rootView);
        combinator.connectToolbar(toolbar);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
        appbarScrollExpander.setExpanded(expanded);
        collapsToolbar.setTitleEnabled(false);

        adapter = new LawsRecyclerViewAdapter(new ArrayList<Law>(), new LawsRecyclerViewAdapter.OnLawClickListener() {
            @Override
            public void onLawClick(Law law) {
                pres.onLawClicked(law);
            }
        });
        recyclerView.setAdapter(adapter);

        AdRequest adRequest = AdRequestHelper.getAdRequest();
        mAdView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.laws_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchText !=null && !searchText.isEmpty()){
            searchItem.expandActionView();
            searchView.setQuery(searchText, false);
            searchView.clearFocus();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pres.onSearchTextSubmitted(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pres.onSearchTextChanged(newText);
                return false;
            }
        });

        sortItem = menu.findItem(R.id.action_sort);
        sortItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                pres.onSortItemClicked();
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        pres.onStop();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        pres.onStart(this);
        pres.load();
        appbarLayout.setExpanded(expanded);
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void prepareToSearch(String searchText) {
        this.searchText = searchText;
        if (searchText !=null && !searchText.isEmpty() && searchItem !=null && searchView !=null){
            searchItem.expandActionView();
            searchView.setQuery(searchText, false);
            searchView.clearFocus();
        }
    }

    @Override
    public void showData(final List<Law> items) {
        adapter.setItems(items);
        if (position> 0) layoutManager.scrollToPosition(position);
        recyclerView.setVisibility(View.VISIBLE);
        mEmptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showDataEmptyMessage() {
        recyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSortDialog(final int currentItemIndex) {
        final String[] sortItems = {
                getResources().getString(R.string.item_sort_lawname),
                getResources().getString(R.string.item_sort_number),
                getResources().getString(R.string.item_sort_date)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_sort);

        builder.setSingleChoiceItems(sortItems, currentItemIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss();
                        pres.sort(currentItemIndex, item);
                    }
                });

        builder.show();
    }

    @Override
    public void showLawDetailsScreen(Law law) {

//        if (mFrameLayoutDetails != null && mDeputy == null){
//            mFrameLayoutDetails.setVisibility(View.VISIBLE);
//            Fragment fragment = LawDetailsFragment.newInstance(law);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
//                    .addToBackStack(null).commit();
//        } else {
//            if (mDeputy == null) {
//                Intent activityIntent = new Intent(this, LawDetailsActivity.class);
//                activityIntent.putExtra(LawDetailsActivity.KEY_LAW, law);
//                startActivity(activityIntent);
        LawDetailsActivity.startScreen(getContext(), law);
//            } else {
//                Intent activityIntent = new Intent(this, DeputyLawDetailsActivity.class);
//                activityIntent.putExtra(LawDetailsActivity.KEY_LAW, law);
//                startActivity(activityIntent);
//            }
//        }

    }

    @Override
    public void showMessage(@StringRes int resId) {
        Snackbar.make(rootView, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


}
