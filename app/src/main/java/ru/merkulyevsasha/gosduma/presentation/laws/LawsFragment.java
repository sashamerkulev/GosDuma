package ru.merkulyevsasha.gosduma.presentation.laws;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.ToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.commons.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsFragment;


public class LawsFragment extends Fragment implements LawsView {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.layout_empty) LinearLayout mEmptyLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.adView) AdView mAdView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @Inject LawsPresenter pres;

    private FrameLayout frameDetails;

    private View rootView;
    private ToolbarCombinator combinator;

    private LawsRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private MenuItem searchItem;
    private SearchView searchView;
    private String searchText;

    private AppbarScrollExpander appbarScrollExpander;
    private int position;
    private boolean expanded;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
        if (context instanceof ToolbarCombinator) {
            combinator = (ToolbarCombinator) context;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_laws, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        combinator.connectToolbar(toolbar);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
        appbarScrollExpander.setExpanded(expanded);
        collapsToolbar.setTitleEnabled(false);

        adapter = new LawsRecyclerViewAdapter(getContext(), new ArrayList<Law>(), new LawsRecyclerViewAdapter.OnLawClickListener() {
            @Override
            public void onLawClick(Law law) {
                pres.onLawClicked(frameDetails==null, law);
            }
        });
        recyclerView.setAdapter(adapter);

        AdRequest adRequest = AdRequestHelper.getAdRequest();
        mAdView.loadAd(adRequest);

        frameDetails = rootView.findViewById(R.id.frame_details);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.laws_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                pres.onSortItemClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        pres.unbind();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        pres.bind(this);
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
    public void onDestroyView() {
        unbinder.unbind();
        pres.onDestroy();
        super.onDestroyView();
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
        if (frameDetails != null) frameDetails.setVisibility(View.INVISIBLE);
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

        //noinspection ConstantConditions
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
        LawDetailsActivity.startScreen(getContext(), law, false);
    }

    @Override
    public void showLawDetailsFragment(Law law) {
        Fragment fragment = LawDetailsFragment.newInstance(law);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_details, fragment)
                .addToBackStack(null)
                .commit();
        frameDetails.setVisibility(View.VISIBLE);
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
