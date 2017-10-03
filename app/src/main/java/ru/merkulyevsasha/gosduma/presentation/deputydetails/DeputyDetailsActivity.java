package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.commons.AppbarScrollExpander;
import ru.merkulyevsasha.gosduma.presentation.commons.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;


public class DeputyDetailsActivity extends AppCompatActivity implements DeputyDetailsView
{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
    @BindView(R.id.collapsinng_toolbar_layout) CollapsingToolbarLayout collapsToolbar;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textview_deputy_name) TextView deputyName;
    @BindView(R.id.textview_position) TextView deputyPosition;
    @BindView(R.id.textview_deputy_fractionName) TextView fractionName;
    @BindView(R.id.textview_deputy_fractionRole) TextView fractionRole;
    @BindView(R.id.textview_deputy_ranks) TextView deputyRanks;
    @BindView(R.id.imageview_photo) ImageView photo;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.layout_empty) LinearLayout emptyLayout;
    @BindView(R.id.details_content) View rootView;

    @Inject DeputyDetailsPresenter pres;

    private LawsRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private Deputy deputy;

    private MenuItem searchItem;
    private MenuItem sortItem;

    private boolean menuItemsVisible = false;

    private AppbarScrollExpander appbarScrollExpander;
    private int position;
    private boolean expanded;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(KeysBundleHolder.KEY_POSITION, layoutManager.findFirstVisibleItemPosition());
        outState.putBoolean(KeysBundleHolder.KEY_MENUITEMSVISIBLE, menuItemsVisible);
        outState.putBoolean(KeysBundleHolder.KEY_EXPANDED, appbarScrollExpander.getExpanded());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(KeysBundleHolder.KEY_POSITION);
        menuItemsVisible = savedInstanceState.getBoolean(KeysBundleHolder.KEY_MENUITEMSVISIBLE);
        expanded = savedInstanceState.getBoolean(KeysBundleHolder.KEY_EXPANDED, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_deputy_details);
        ButterKnife.bind(this);
        GosDumaApp.getComponent().inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        deputy = intent.getParcelableExtra(KeysBundleHolder.KEY_DEPUTY);
        if (deputy == null) finish();

        setTitle(deputy.name);

        collapsToolbar.setTitleEnabled(false);
        appbarScrollExpander = new AppbarScrollExpander(recyclerView, appbarLayout);
        appbarScrollExpander.setExpanded(expanded);


//        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
//                    // Collapsed
//                    setCollapsingToolbarTitleEnabled(true);
//                } else if (verticalOffset == 0) {
//                    // Expanded
//                    setCollapsingToolbarTitleEnabled(false);
//                } else {
//                    // Somewhere in between
//                    setCollapsingToolbarTitleEnabled(false);
//                }
//            }
//        });

        deputyPosition.setText(deputy.getPositionWithStartAndEndDates());

        deputyName.setText(deputy.getNameWithBirthday());
        String ranksAndDegrees = deputy.getRanksWithDegrees();
        if (ranksAndDegrees == null || ranksAndDegrees.isEmpty()){
            deputyRanks.setVisibility(View.GONE);
        } else {
            deputyRanks.setText(ranksAndDegrees);
            deputyRanks.setVisibility(View.VISIBLE);
        }
        fractionName.setText(deputy.fractionName);
        fractionRole.setText(deputy.fractionRole + " " + deputy.fractionRegion);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(true);

        adapter = new LawsRecyclerViewAdapter(new ArrayList<Law>(), new LawsRecyclerViewAdapter.OnLawClickListener() {
            @Override
            public void onLawClick(Law law) {
                pres.onLawClicked(law);
            }
        });
        recyclerView.setAdapter(adapter);

        try {
            Picasso.with(this).load(getResources().getIdentifier("b"+String.valueOf(deputy.id), "raw", getPackageName()))
                    .into(photo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pres.onSharedClicked(deputy);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.deputy_details_menu, menu);

        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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

        sortItem = menu.findItem(R.id.action_sort);
        sortItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                pres.onSortItemClicked();
                return false;
            }
        });

        return true;
    }

    @Override
    public void showData(final List<Law> items) {
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
        LawDetailsActivity.startScreen(this, law);
    }

    @Override
    public void share(Deputy deputy) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        final StringBuilder message = new StringBuilder();

        message.append(deputy.getNameWithBirthday());
        message.append("\n");
        message.append(deputy.getPositionWithStartAndEndDates());
        message.append("\n");
        if (!deputy.getRanksWithDegrees().isEmpty()) {
            message.append(deputy.getRanksWithDegrees());
            message.append("\n");
        }
        message.append(deputy.fractionName);
        message.append("\n");
        message.append(deputy.fractionRole);
        if (!deputy.fractionRegion.isEmpty()){
            message.append(" ");
            message.append(deputy.fractionRegion);
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
    }

    @Override
    public void showSortDialog(final int currentItemIndex) {
        final String[] sortItems = {
                getResources().getString(R.string.item_sort_lawname),
                getResources().getString(R.string.item_sort_number),
                getResources().getString(R.string.item_sort_date)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void onStop() {
        super.onStop();
        pres.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        pres.onStart(this);
        pres.load(deputy.id);
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

    public static void startScreen(Context context, Deputy deputy) {
        Intent activityIntent = new Intent(context, DeputyDetailsActivity.class);
        activityIntent.putExtra(KeysBundleHolder.KEY_DEPUTY, deputy);
        context.startActivity(activityIntent);
    }
}
