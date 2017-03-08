package ru.merkulyevsasha.gosduma;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

import ru.merkulyevsasha.apprate.AppRateRequester;
import ru.merkulyevsasha.gosduma.presentation.MvpFragment;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesView;
import ru.merkulyevsasha.gosduma.presentation.deputyrequests.DeputyRequestsView;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsView;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails.DeputyRequestDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails.DeputyRequestDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.DeputyLawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesFragment;
import ru.merkulyevsasha.gosduma.presentation.PresenterInterface;
import ru.merkulyevsasha.gosduma.presentation.ViewInterface;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.deputyrequests.DeputyRequestsFragment;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsFragment;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsFragment;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;

import static ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity.KEY_DEPUTY;
import static ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails.DeputyRequestDetailsActivity.KEY_DEPUTYREQUEST;
import static ru.merkulyevsasha.gosduma.presentation.lawdetails.BaseLawDetailsActivity.KEY_LAW;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
        , MenuItem.OnMenuItemClickListener
        , SearchView.OnQueryTextListener
        , ViewInterface
        , ViewInterface.OnPresenterListener
        , DeputiesView.OnDeputyClickListener
        , LawsView.OnLawClickListener
        , DeputyRequestsView.OnDeputyRequestsClickListener
{

    public final static String KEY_ID = "ID";
    public final static String KEY_NAME = "NAME";
    private final static String KEY_TITLE = "TITLE";
    private final static String KEY_SEARCHTEXT = "SEARCHTEXT";
    private final static String KEY_ITEMID = "ITEMID";

    private MvpFragment mFragment;

    private Law mLaw;
    private Deputy mDeputy;
    private DeputyRequest mDeputyRequest;

    private MenuItem mFilterItem;
    private MenuItem mSortItem;
    private MenuItem mSearchItem;
    private SearchView mSearchView;

    private ProgressBar mProgress;

    private FrameLayout mFrameLayout;

    private String mTitle;
    private String mSearchText;

    private int mItemId;

    private FrameLayout mFrameLayoutDetails;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        if (mDeputy != null) {
            outState.putParcelable(KEY_DEPUTY, mDeputy);
        }
        if (mLaw != null) {
            outState.putParcelable(KEY_LAW, mLaw);
        }
        if (mDeputyRequest != null) {
            outState.putParcelable(KEY_DEPUTYREQUEST, mDeputyRequest);
        }
        outState.putString(KEY_TITLE, mTitle);
        outState.putString(KEY_SEARCHTEXT, mSearchText);
        outState.putInt(KEY_ITEMID, mItemId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchViewText();            }
        });

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_searchlist);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFrameLayoutDetails = (FrameLayout) findViewById(R.id.frame_searchdetails);
        hideFragmentDetails();

        if (savedInstanceState == null) {
            //mItemId = R.id.nav_deputies;
            //setDeputiesFragment();
            mItemId = R.id.nav_laws;
            setLawsFragment();
        } else {
            mItemId = savedInstanceState.getInt(KEY_ITEMID);
            mTitle = savedInstanceState.getString(KEY_TITLE);
            setTitle(mTitle);

            if (savedInstanceState.containsKey(KEY_SEARCHTEXT)) {
                mSearchText = savedInstanceState.getString(KEY_SEARCHTEXT);
            }

            if (savedInstanceState.containsKey(KEY_LAW) && mItemId == R.id.nav_laws) {
                mLaw = savedInstanceState.getParcelable(KEY_LAW);
                showLawDetails(mLaw);
            }
            if (savedInstanceState.containsKey(KEY_DEPUTY) && mItemId == R.id.nav_deputies) {
                mDeputy = savedInstanceState.getParcelable(KEY_DEPUTY);
                showDeputyDetails(mDeputy);
            }
            if (savedInstanceState.containsKey(KEY_DEPUTYREQUEST) && mItemId == R.id.nav_depqueries) {
                mDeputyRequest = savedInstanceState.getParcelable(KEY_DEPUTYREQUEST);
                showDeputyRequestDetails(mDeputyRequest);
            }
        }

        AppRateRequester.Run(this, BuildConfig.APPLICATION_ID);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFilterItem.setVisible(false);
                mSortItem.setVisible(false);
                mFrameLayout.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                setVisibleMenuItems();
            }
        });
    }

    @Override
    public void showMessage(int resId) {

    }

    private void setVisibleMenuItems(){
        if (mFragment != null) {
            if (mSortItem != null) {
                mSortItem.setVisible(mFragment.isSortMenuVisible() && mProgress.getVisibility() == View.GONE);
            }
            if (mFilterItem != null) {
                mFilterItem.setVisible(mFragment.isFilterMenuVisible() && mProgress.getVisibility() == View.GONE);
            }
        }
    }

    private void searchViewText() {
        mSearchItem.expandActionView();
        mSearchView.setQuery(mSearchText, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        mSearchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        if (mSearchText != null && !mSearchText.isEmpty()) {
            searchViewText();
        }
        mSearchView.setOnQueryTextListener(this);

        mFilterItem = menu.findItem(R.id.action_filter);
        mFilterItem.setOnMenuItemClickListener(this);

        mSortItem = menu.findItem(R.id.action_sort);
        mSortItem.setOnMenuItemClickListener(this);

        setVisibleMenuItems();

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mItemId = item.getItemId();

        mLaw = null;
        mDeputy = null;
        mDeputyRequest = null;

        if (mItemId == R.id.nav_news_gd) {
            startNewsActivity(R.id.nav_news_gd, item.getTitle().toString());
        } else if (mItemId == R.id.nav_news_preds) {
            startNewsActivity(R.id.nav_news_preds, item.getTitle().toString());
        }

        else if (mItemId == R.id.nav_comittee) {
            startListActivity(R.id.nav_comittee, item.getTitle().toString());
        } else if (mItemId == R.id.nav_blocks) {
            startListActivity(R.id.nav_blocks, item.getTitle().toString());
        } else if (mItemId == R.id.nav_otras) {
            startListActivity(R.id.nav_otras, item.getTitle().toString());
        } else if (mItemId == R.id.nav_reg) {
            startListActivity(R.id.nav_reg, item.getTitle().toString());
        } else if (mItemId == R.id.nav_fed) {
            startListActivity(R.id.nav_fed, item.getTitle().toString());
        } else if (mItemId == R.id.nav_stad) {
            startListActivity(R.id.nav_stad, item.getTitle().toString());
        } else if (mItemId == R.id.nav_inst) {
            startListActivity(R.id.nav_inst, item.getTitle().toString());
        }

        else if (mItemId == R.id.nav_deputies) {
            hideFragmentDetails();
            showNavFragment(R.id.nav_deputies);
            closeDrawer();
        } else if (mItemId == R.id.nav_laws) {
            hideFragmentDetails();
            showNavFragment(R.id.nav_laws);
            closeDrawer();
        } else if (mItemId == R.id.nav_depqueries) {
            hideFragmentDetails();
            showNavFragment(R.id.nav_depqueries);
            closeDrawer();
        }

        return true;
    }

    private void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void hideFragmentDetails(){
        if (mFrameLayoutDetails != null) {
            mFrameLayoutDetails.setVisibility(View.INVISIBLE);
        }
    }

    private void startActivity(int id, String name, Class<?> cls){
        Intent activityIntent = new Intent(this, cls);
        activityIntent.putExtra(KEY_ID, id);
        activityIntent.putExtra(KEY_NAME, name);
        startActivity(activityIntent);
    }

    private void startNewsActivity(int id, String name){
        startActivity(id, name, NewsActivity.class);
    }

    private void startListActivity(int id, String name){
        startActivity(id, name, ListDataActivity.class);
    }

    private void setDeputiesFragment(){
        mTitle = getString(R.string.menu_deputies);
        setTitle(mTitle);
        mFragment = new DeputiesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, (Fragment)mFragment)
                .commit();
    }

    private void setLawsFragment(){
        mTitle = getString(R.string.menu_laws);
        setTitle(mTitle);
        mFragment = new LawsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, (Fragment)mFragment)
                .commit();
    }

    private void setDeputyRequestsFragment(){
        mTitle = getString(R.string.menu_deputies_requests);
        setTitle(mTitle);
        mFragment = new DeputyRequestsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, (Fragment)mFragment)
                .commit();
    }

    private void showNavFragment(int id){

        if (id == R.id.nav_deputies) {
            setDeputiesFragment();
        } else if (id == R.id.nav_laws) {
            setLawsFragment();
        } else if (id == R.id.nav_depqueries) {
            setDeputyRequestsFragment();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchText = query;
        mFragment.search(query);
        setVisibleMenuItems();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()){
            mSearchText = newText;
            mFragment.search(newText);
            setVisibleMenuItems();
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.action_sort) {

            int sortDialogType = mFragment.getSortDialogType();
            if (sortDialogType == DialogHelper.IDD_DEPUTY_SORT) {
                DialogHelper.getDeputySortDialog(this,
                        mFragment.getCurrentSortIndexValue().get(0),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mFragment.sort(mFragment.getCurrentSortIndexValue(), selectItemsIndex);
                            }
                        }
                ).show();
            }

            if (sortDialogType == DialogHelper.IDD_LAWS_SORT) {
                DialogHelper.getLawSortDialog(this,
                        mFragment.getCurrentSortIndexValue().get(0),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mFragment.sort(mFragment.getCurrentSortIndexValue(), selectItemsIndex);
                            }
                        }
                ).show();
            }

            if (sortDialogType == DialogHelper.IDD_DEPUTY_REQUEST_SORT) {
                DialogHelper.getDeputyRequestsSortDialog(this,
                        mFragment.getCurrentSortIndexValue().get(0),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mFragment.sort(mFragment.getCurrentSortIndexValue(), selectItemsIndex);
                            }
                        }
                ).show();
            }


            return false;
        }

        if (menuItem.getItemId() == R.id.action_filter) {

            int filterDialogType = mFragment.getFilterDialogType();
            if (filterDialogType == DialogHelper.IDD_DEPUTY_FILTER){
                DialogHelper.getDeputyFilterDialog(this,
                        mFragment.getCurrentFilterIndexValue(),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mFragment.filter(selectItemsIndex);
                            }
                        }
                ).show();
            }

            return false;
        }

        return false;
    }

    @Override
    public void onPresenterCreated(PresenterInterface presenter) {
//        mPresenter = presenter;
        setVisibleMenuItems();
    }

    private void showDeputyDetails(Deputy deputy){
        if (mFrameLayoutDetails != null){
            mFrameLayoutDetails.setVisibility(View.VISIBLE);
            Fragment fragment = DeputyDetailsFragment.newInstance(deputy);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
                    .addToBackStack(null).commit();
        } else {
            Intent activityIntent = new Intent(this, DeputyDetailsActivity.class);
            activityIntent.putExtra(KEY_DEPUTY, deputy);
            startActivity(activityIntent);
        }
    }

    private void showLawDetails(Law law){
        if (mFrameLayoutDetails != null && mDeputy == null){
            mFrameLayoutDetails.setVisibility(View.VISIBLE);
            Fragment fragment = LawDetailsFragment.newInstance(law);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
                    .addToBackStack(null).commit();
        } else {
            if (mDeputy == null) {
                Intent activityIntent = new Intent(this, LawDetailsActivity.class);
                activityIntent.putExtra(KEY_LAW, law);
                startActivity(activityIntent);
            } else {
                Intent activityIntent = new Intent(this, DeputyLawDetailsActivity.class);
                activityIntent.putExtra(KEY_LAW, law);
                startActivity(activityIntent);
            }
        }
    }

    private void showDeputyRequestDetails(DeputyRequest deputyRequest){
        if (mFrameLayoutDetails != null){
            mFrameLayoutDetails.setVisibility(View.VISIBLE);
            Fragment fragment = DeputyRequestDetailsFragment.newInstance(deputyRequest);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment)
                    .addToBackStack(null).commit();
        } else {
            Intent activityIntent = new Intent(this, DeputyRequestDetailsActivity.class);
            activityIntent.putExtra(KEY_DEPUTYREQUEST, deputyRequest);
            startActivity(activityIntent);
        }
    }

    @Override
    public void onDeputyClick(Deputy deputy) {
        mDeputy = deputy;
        showDeputyDetails(deputy);
    }

    @Override
    public void onLawClick(Law law) {
        mLaw = law;
        showLawDetails(law);
    }

    @Override
    public void onDeputyRequestwClick(DeputyRequest deputyRequest) {
        mDeputyRequest = deputyRequest;
        showDeputyRequestDetails(deputyRequest);
    }
}
