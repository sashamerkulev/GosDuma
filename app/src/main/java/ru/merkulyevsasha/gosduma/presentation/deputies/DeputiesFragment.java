package ru.merkulyevsasha.gosduma.presentation.deputies;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.DrawerToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity;


public class DeputiesFragment extends Fragment implements DeputiesView {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.layout_empty) LinearLayout emptyLayout;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.adView) AdView adView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @Inject DeputiesPresenter pres;

    private View rootView;
    private DrawerToolbarCombinator combinator;

    private DeputiesRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    private MenuItem filterItem;
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
        rootView = inflater.inflate(R.layout.fragment_deputies, container, false);
        ButterKnife.bind(this, rootView);
        combinator.connectToolbar(toolbar);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
        appbarScrollExpander.setExpanded(expanded);
        collapsToolbar.setTitleEnabled(false);

        adapter = new DeputiesRecyclerViewAdapter(getActivity(), new ArrayList<Deputy>(), new OnDeputyClickListener() {
            @Override
            public void onDeputyClick(Deputy deputy) {
                pres.onDeputyClicked(deputy);
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

        inflater.inflate(R.menu.deputies_menu, menu);

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

        filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                pres.onFilterItemClicked();
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
    public void showData(final List<Deputy> items) {
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
    public void showDeputyDetailsScreen(Deputy deputy) {
//        if (mFrameLayoutDetails != null){
//            mFrameLayoutDetails.setVisibility(View.VISIBLE);
//            Fragment fragment = DeputyDetailsFragment.newInstance(deputy);
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
//                    .addToBackStack(null).commit();
//        } else {
        DeputyDetailsActivity.startScreen(getContext(), deputy);
//        }
    }

    @Override
    public void showFilterDialog(List<Integer> filterSettings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.title_filter);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_deputy_filter, null);
        builder.setView(view);
        final RadioButton rb_deputy = (RadioButton)view.findViewById(R.id.radiobox_deputy_gd);
        final RadioButton rb_member = (RadioButton)view.findViewById(R.id.radiobox_member);
        final RadioButton rb_working = (RadioButton)view.findViewById(R.id.radiobox_working);
        final RadioButton rb_not_working = (RadioButton)view.findViewById(R.id.radiobox_not_working);

        final int deputy =  filterSettings.get(0);

        rb_deputy.setChecked(deputy == DeputiesPresenter.DEPUTY_INDEX);
        rb_member.setChecked(deputy == DeputiesPresenter.MEMBER_INDEX);

        final int working =  filterSettings.get(1);
        rb_working.setChecked(working == DeputiesPresenter.WORKING_INDEX);
        rb_not_working.setChecked(working == DeputiesPresenter.NOT_WORKING_INDEX);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            public void onClick(DialogInterface dialog, int whichButton) {
                List<Integer> newFilterSettings = new ArrayList<>();
                if (rb_deputy.isChecked()){
                    newFilterSettings.add(DeputiesPresenter.DEPUTY_INDEX);
                } else {
                    if (rb_member.isChecked()) {
                        newFilterSettings.add(DeputiesPresenter.MEMBER_INDEX);
                    }
                }
                if (rb_working.isChecked()){
                    newFilterSettings.add(DeputiesPresenter.WORKING_INDEX);
                } else {
                    if (rb_not_working.isChecked()) {
                        newFilterSettings.add(DeputiesPresenter.NOT_WORKING_INDEX);
                    }
                }
                dialog.dismiss();
                pres.filter(newFilterSettings);
            }
        });

        builder.show();
    }

    @Override
    public void showSortDialog(final int currentItemIndex) {

        final String[] sortItems = {
                getResources().getString(R.string.item_sort_name),
                getResources().getString(R.string.item_sort_birtdate),
                getResources().getString(R.string.item_sort_fractionname)};

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

    private interface OnDeputyClickListener{
        void onDeputyClick(Deputy deputy);
    }

    private class DeputiesRecyclerViewAdapter extends RecyclerView.Adapter<DeputiesRecyclerViewAdapter.DeputiesViewHolder> {

        private final List<Deputy> items;

        private final OnDeputyClickListener onDeputyClickListener;
        private final Context context;

        DeputiesRecyclerViewAdapter(Context context, List<Deputy> items, OnDeputyClickListener onDeputyClickListener){
            this.items = items;
            this.onDeputyClickListener = onDeputyClickListener;
            this.context = context;
        }

        @Override
        public DeputiesRecyclerViewAdapter.DeputiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deputy_item, parent, false);
            return new DeputiesRecyclerViewAdapter.DeputiesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DeputiesRecyclerViewAdapter.DeputiesViewHolder holder, int position) {

            final Deputy item = items.get(position);

            String fractionName = item.fractionName;
            String fractionRole = item.fractionRole;
            if (fractionRole != null) fractionRole = fractionRole.trim();

            holder.deputyName.setText(item.getNameWithBirthday());
            holder.deputyPosition.setText(item.getCurrentPosition());

            if (!fractionRole.isEmpty()) {
                holder.deputyFractionName.setVisibility(View.VISIBLE);
                holder.deputyFractionName.setText(fractionRole + " (" + fractionName + ")");
            } else {
                holder.deputyFractionName.setVisibility(View.GONE);
            }

            try {
                Picasso.with(context)
                        .load(context.getResources().getIdentifier("b"+String.valueOf(item.id), "raw", context.getPackageName()))
                        .into(holder.photo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeputyClickListener.onDeputyClick(item);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(List<Deputy> items){
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        class DeputiesViewHolder extends RecyclerView.ViewHolder{

            final TextView deputyName;
            final TextView deputyPosition;
            final TextView deputyFractionName;
            final ImageView photo;

            DeputiesViewHolder(final View itemView) {
                super(itemView);
                deputyName = (TextView)itemView.findViewById(R.id.deputy_name);
                deputyFractionName = (TextView)itemView.findViewById(R.id.deputy_fraction_name);
                deputyPosition = (TextView)itemView.findViewById(R.id.deputy_position);
                photo = (ImageView)itemView.findViewById(R.id.imageview_photo);
            }
        }

    }

}
