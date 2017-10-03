package ru.merkulyevsasha.gosduma.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.apprate.AppRateRequester;
import ru.merkulyevsasha.gosduma.BuildConfig;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.preferences.SettingsSharedPreferences;
import ru.merkulyevsasha.gosduma.presentation.DrawerToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.listdata.ListDataActivity;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesFragment;
import ru.merkulyevsasha.gosduma.presentation.deputyrequests.DeputyRequestsFragment;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsFragment;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;
import ru.merkulyevsasha.gosduma.presentation.services.GDJob;


public class MainActivity extends AppCompatActivity
        implements DrawerToolbarCombinator, NavigationView.OnNavigationItemSelectedListener
{
    private static final String DEPUTIES_TAG_FRAGMENT = "deputies";
    private static final String DEPUTIES_REQUESTS_TAG_FRAGMENT = "deputies_requests";
    private static final String LAWS_TAG_FRAGMENT = "laws";


    @BindView(R.id.progressBar) ProgressBar mProgress;
    @BindView(R.id.frame_searchlist) FrameLayout mFrameLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
//    @BindView(R.id.frame_searchdetails) FrameLayout mFrameLayoutDetails;
    private FrameLayout mFrameLayoutDetails;

    @Inject SettingsSharedPreferences prefs;

    private ActionBarDrawerToggle drawerToggle;

    private Fragment mFragment;

    private String mTitle;

    private int navItemId;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putString(KeysBundleHolder.KEY_TITLE, mTitle);
        outState.putInt(KeysBundleHolder.KEY_ITEMID, navItemId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        navItemId = savedInstanceState.getInt(KeysBundleHolder.KEY_ITEMID);
        mTitle = savedInstanceState.getString(KeysBundleHolder.KEY_TITLE);
        setTitle(mTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        GosDumaApp.getComponent().inject(this);

        mFrameLayoutDetails = (FrameLayout) findViewById(R.id.frame_searchdetails);

        navigationView.setNavigationItemSelectedListener(this);

//        hideFragmentDetails();
        if (savedInstanceState == null) setDeputiesFragment();

        AppRateRequester.Run(this, BuildConfig.APPLICATION_ID);

        if (prefs.getFirstRunFlag()){
            GDJob.scheduleJob();
            prefs.setFirstRunFlag();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navItemId = item.getItemId();

        switch(navItemId){
            case R.id.nav_news_gd:
            case R.id.nav_news_preds:
            case R.id.nav_akt_pres:
            case R.id.nav_akt_gover:
            case R.id.nav_akt_sf:
            case R.id.nav_akt_gd:
                startNewsActivity(navItemId, item.getTitle().toString());
                break;
            case R.id.nav_comittee:
            case R.id.nav_blocks:
            case R.id.nav_otras:
            case R.id.nav_reg:
            case R.id.nav_fed:
            case R.id.nav_stad:
            case R.id.nav_inst:
                startListActivity(navItemId, item.getTitle().toString());
                break;

            case R.id.nav_deputies:
            case R.id.nav_laws:
            case R.id.nav_depqueries:
                //hideFragmentDetails();
                if (navItemId == R.id.nav_deputies) {
                    setDeputiesFragment();
                } else if (navItemId == R.id.nav_laws) {
                    setLawsFragment();
                } else if (navItemId == R.id.nav_depqueries) {
                    setDeputyRequestsFragment();
                }
                closeDrawer();
        }

        return true;
    }

    private void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void startActivity(int id, String name, Class<?> cls){
        Intent activityIntent = new Intent(this, cls);
        activityIntent.putExtra(KeysBundleHolder.KEY_ID, id);
        activityIntent.putExtra(KeysBundleHolder.KEY_NAME, name);
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
        mFragment = getSupportFragmentManager().findFragmentByTag(DEPUTIES_TAG_FRAGMENT);
        if (mFragment == null) mFragment = new DeputiesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, mFragment, DEPUTIES_TAG_FRAGMENT)
                .commit();
    }

    private void setLawsFragment(){
        mTitle = getString(R.string.menu_laws);
        setTitle(mTitle);
        mFragment = getSupportFragmentManager().findFragmentByTag(LAWS_TAG_FRAGMENT);
        if (mFragment == null) mFragment = new LawsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, mFragment, LAWS_TAG_FRAGMENT)
                .commit();
    }

    private void setDeputyRequestsFragment(){
        mTitle = getString(R.string.menu_deputies_requests);
        setTitle(mTitle);
        mFragment = getSupportFragmentManager().findFragmentByTag(DEPUTIES_REQUESTS_TAG_FRAGMENT);
        if (mFragment == null) mFragment = new DeputyRequestsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, mFragment, DEPUTIES_REQUESTS_TAG_FRAGMENT)
                .commit();
    }

    @Override
    public void connectToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

}
