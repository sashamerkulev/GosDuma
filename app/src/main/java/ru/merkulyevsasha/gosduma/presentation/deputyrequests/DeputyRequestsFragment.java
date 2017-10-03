package ru.merkulyevsasha.gosduma.presentation.deputyrequests;

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
import android.widget.TextView;

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
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.DrawerToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails.DeputyRequestDetailsActivity;


public class DeputyRequestsFragment extends Fragment implements DeputyRequestsView {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.layout_empty) LinearLayout emptyLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.adView) AdView adView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @Inject DeputyRequestsPresenter pres;

    private View rootView;
    private DrawerToolbarCombinator combinator;

    private DeputyRequestsRecyclerViewAdapter adapter;
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
        rootView = inflater.inflate(R.layout.fragment_deputyrequests, container, false);
        ButterKnife.bind(this, rootView);
        combinator.connectToolbar(toolbar);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
        appbarScrollExpander.setExpanded(expanded);
        collapsToolbar.setTitleEnabled(false);

        adapter = new DeputyRequestsRecyclerViewAdapter(new ArrayList<DeputyRequest>(), new OnDeputyRequestsClickListener() {
            @Override
            public void onDeputyRequestwClick(DeputyRequest deputyRequest) {
                pres.onDeputyRequestClicked(deputyRequest);
            }
        });
        recyclerView.setAdapter(adapter);

        AdRequest adRequest = AdRequestHelper.getAdRequest();
        adView.loadAd(adRequest);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.deputiesrequests_menu, menu);

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
        if (adView != null) {
            adView.pause();
        }
        pres.onStop();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        pres.onStart(this);
        pres.load();
        appbarLayout.setExpanded(expanded);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
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
    public void showSortDialog(final int currentItemIndex) {

        final String[] sortItems = {
                getResources().getString(R.string.item_sort_lawname),
                getResources().getString(R.string.item_sort_date),
                getResources().getString(R.string.item_sort_initiator)};

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
    public void showData(final List<DeputyRequest> items) {
        adapter.setItems(items);
        if (position> 0) layoutManager.scrollToPosition(position);
        recyclerView.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
    }

    @Override
    public void showDataEmptyMessage() {
        recyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDeputyRequestDetailsScreen(DeputyRequest deputyRequest) {

//        if (mFrameLayoutDetails != null){
//            mFrameLayoutDetails.setVisibility(View.VISIBLE);
//            Fragment fragment = DeputyRequestDetailsFragment.newInstance(deputyRequest);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
//                    .addToBackStack(null).commit();
//        } else {
        DeputyRequestDetailsActivity.startScreen(getContext(), deputyRequest);
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

    interface OnDeputyRequestsClickListener {
        void onDeputyRequestwClick(DeputyRequest deputyRequest);
    }

    class DeputyRequestsRecyclerViewAdapter extends RecyclerView.Adapter<DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder> {

        private final List<DeputyRequest> items;
        private final OnDeputyRequestsClickListener onDeputyRequestsClickListener;

        DeputyRequestsRecyclerViewAdapter(List<DeputyRequest> items, OnDeputyRequestsClickListener onDeputyRequestsClickListener){
            this.items = items;
            this.onDeputyRequestsClickListener = onDeputyRequestsClickListener;
        }

        @Override
        public DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deputy_request_item, parent, false);
            return new DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder holder, int position) {

            final DeputyRequest deputyRequest = items.get(position);

            holder.deputyRequestName.setText(deputyRequest.getNameWithNumberAndDate());
            holder.deputyRequestInitiator.setText(deputyRequest.initiator);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeputyRequestsClickListener.onDeputyRequestwClick(deputyRequest);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<DeputyRequest> items){
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        class DeputyRequestsViewHolder extends RecyclerView.ViewHolder{

            final TextView deputyRequestName;
            final TextView deputyRequestInitiator;

            DeputyRequestsViewHolder(final View itemView) {
                super(itemView);
                deputyRequestName = (TextView)itemView.findViewById(R.id.deputyrequest_name);
                deputyRequestInitiator = (TextView)itemView.findViewById(R.id.deputyrequest_initiator);

            }

        }

    }

}
