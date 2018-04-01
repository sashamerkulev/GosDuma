package ru.merkulyevsasha.gosduma.presentation.deputydetails;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.ToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.commons.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;


public class DeputyDetailsFragment extends Fragment implements DeputyDetailsView {

    public final static String KEY_DEPUTY_FRAGMENT = "DEPUTY_FRAGMENT";

    @Inject DeputyDetailsPresenter pres;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;

    @BindView(R.id.progressbar) ProgressBar progressbar;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.layout_empty) LinearLayout emptyLayout;

    private View rootView;
    private Deputy deputy;
    private LawsRecyclerViewAdapter adapter;
    private ToolbarCombinator combinator;
    private AppbarScrollExpander appbarScrollExpander;
    private boolean expanded;

    private Bundle args;
    private boolean deputyFragment;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        if (args != null) {
            deputyFragment = args.getBoolean(DeputyDetailsFragment.KEY_DEPUTY_FRAGMENT, false);
            setHasOptionsMenu(!deputyFragment);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarCombinator) {
            combinator = (ToolbarCombinator) context;
        }
    }

    public static DeputyDetailsFragment newInstance(Deputy deputy, boolean deputyFragment) {
        DeputyDetailsFragment fragment = new DeputyDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KeysBundleHolder.KEY_DEPUTY, deputy);
        args.putBoolean(DeputyDetailsFragment.KEY_DEPUTY_FRAGMENT, deputyFragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_deputy_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        AndroidSupportInjection.inject(this);

        if (args == null) return rootView;
        deputy = args.getParcelable(KeysBundleHolder.KEY_DEPUTY);
        if (deputy == null) return rootView;

        if (deputyFragment){
            toolbar.setVisibility(View.GONE);
        } else {
            appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
            appbarScrollExpander.setExpanded(expanded);
            collapsToolbar.setTitleEnabled(false);

            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(deputy.name);
            combinator.connectToolbar(toolbar);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        adapter = new LawsRecyclerViewAdapter(getContext(), new ArrayList<Law>(), new LawsRecyclerViewAdapter.OnLawClickListener() {
            @Override
            public void onLawClick(Law law) {
                pres.onLawClicked(law);
            }
        });
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.deputy_details_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pres.onSearchTextSubmitted(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pres.onSearchTextChanged(deputy.id, newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
            case R.id.action_sort:
                pres.onSortItemClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        pres.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        pres.bind(this);
        pres.load(deputy.id);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        pres.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void showData(final List<Law> items) {
        adapter.setDeputyItem(deputy);
        adapter.setItems(items);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showDataEmptyMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLawDetailsScreen(Law law) {
        LawDetailsActivity.startScreen(getContext(), law, true);
    }

    @Override
    public void showSortDialog(final int currentItemIndex) {
        final String[] sortItems = {
                getResources().getString(R.string.item_sort_lawname),
                getResources().getString(R.string.item_sort_number),
                getResources().getString(R.string.item_sort_date)};

        @SuppressWarnings("ConstantConditions") AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    public void showMessage(@StringRes int resId) {
        Snackbar.make(rootView, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

}
