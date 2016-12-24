package ru.merkulyevsasha.gosduma;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.OnDeputyClickListener;
import ru.merkulyevsasha.gosduma.mvp.OnLawClickListener;
import ru.merkulyevsasha.gosduma.mvp.deputies.DeputiesFragment;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;
import ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsActivity;
import ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsFragment;
import ru.merkulyevsasha.gosduma.mvp.laws.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsFragment;

import static ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsActivity.KEY_DEPUTY;
import static ru.merkulyevsasha.gosduma.mvp.laws.BaseLawDetailsActivity.KEY_LAW;

public class MainActivity extends AppCompatActivity
        implements
        NavigationView.OnNavigationItemSelectedListener
        , MenuItem.OnMenuItemClickListener
        , SearchView.OnQueryTextListener
        , ViewInterface.OnPresenterListener
        , OnDeputyClickListener
        , OnLawClickListener
{

    public final static String KEY_ID = "ID";
    public final static String KEY_NAME = "NAME";

    private PresenterInterface mPresenter;

    private Law mLaw;
    private Deputy mDeputy;

    private MenuItem mFilterItem;

    private MenuItem mSortItem;


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

//        Bundle state = mPresenter.getState();
//        if (state != null){
//            outState.putAll(state);
//        }
        if (mDeputy != null) {
            outState.putParcelable(KEY_DEPUTY, mDeputy);
        }
        if (mLaw != null) {
            outState.putParcelable(KEY_LAW, mLaw);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        AssetManager assetManager = this.getAssets();
        try {
            File fileDb = new File(this.getFilesDir(), DatabaseHelper.DATABASE_NAME);
            if (!fileDb.exists()) {

                InputStream in = assetManager.open(DatabaseHelper.DATABASE_NAME);
                FileOutputStream out = new FileOutputStream(fileDb);

                byte[] buffer = new byte[1024000];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();
            }

        } catch (IOException e) {
            FirebaseCrash.report(e);
        }

        if (savedInstanceState == null) {
            setDeputiesFragment();
        } else {
            if (savedInstanceState.containsKey(KEY_LAW)) {
                mLaw = savedInstanceState.getParcelable(KEY_LAW);
            }
            if (savedInstanceState.containsKey(KEY_DEPUTY)) {
                mDeputy = savedInstanceState.getParcelable(KEY_DEPUTY);
                showDeputyDetails(mDeputy);
            }
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

        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

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
        int id = item.getItemId();

        if (id == R.id.nav_news_gd) {
            startNewsActivity(R.id.nav_news_gd, item.getTitle().toString());
        } else if (id == R.id.nav_news_preds) {
            startNewsActivity(R.id.nav_news_preds, item.getTitle().toString());
        }

        else if (id == R.id.nav_comittee) {
            startListActivity(R.id.nav_comittee, item.getTitle().toString());
        } else if (id == R.id.nav_blocks) {
            startListActivity(R.id.nav_blocks, item.getTitle().toString());
        } else if (id == R.id.nav_otras) {
            startListActivity(R.id.nav_otras, item.getTitle().toString());
        } else if (id == R.id.nav_reg) {
            startListActivity(R.id.nav_reg, item.getTitle().toString());
        } else if (id == R.id.nav_fed) {
            startListActivity(R.id.nav_fed, item.getTitle().toString());
        } else if (id == R.id.nav_stad) {
            startListActivity(R.id.nav_stad, item.getTitle().toString());
        } else if (id == R.id.nav_inst) {
            startListActivity(R.id.nav_inst, item.getTitle().toString());
        }

        else if (id == R.id.nav_deputies) {
            showNavFragment(R.id.nav_deputies);
        } else if (id == R.id.nav_laws) {
            showNavFragment(R.id.nav_laws);
        } else if (id == R.id.nav_depqueries) {
            showNavFragment(R.id.nav_depqueries);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        startActivity(id, name, ListActivity.class);
    }

    private void setDeputiesFragment(){
        setTitle(R.string.menu_deputies);
        DeputiesFragment fragment = new DeputiesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, fragment)
                .commit();
    }

    private void setLawsFragment(){
        setTitle(R.string.menu_laws);
        LawsFragment fragment = new LawsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_searchlist, fragment)
                .commit();
    }

    private void showNavFragment(int id){

        if (id == R.id.nav_deputies) {
            setDeputiesFragment();
        } else if (id == R.id.nav_laws) {
            setLawsFragment();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()){
            mPresenter.search(newText);
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.action_sort) {

            int sortDialogType = mPresenter.getSortDialogType();
            if (sortDialogType == DialogHelper.IDD_DEPUTY_SORT) {
                DialogHelper.getDeputySortDialog(this,
                        mPresenter.getCurrentSortIndexValue().get(0),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mPresenter.sort(mPresenter.getCurrentSortIndexValue(), selectItemsIndex);
                            }
                        }
                ).show();
            }

            return false;
        }

        if (menuItem.getItemId() == R.id.action_filter) {

            int filterDialogType = mPresenter.getFilterDialogType();
            if (filterDialogType == DialogHelper.IDD_DEPUTY_FILTER){
                DialogHelper.getDeputyFilterDialog(this,
                        mPresenter.getCurrentFilterIndexValue(),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mPresenter.filter(selectItemsIndex);
                            }
                        }
                ).show();
            }

            return false;
        }

        return false;
    }

    private void setVisibleMenuItems(){
        if (mPresenter != null) {
            if (mSortItem != null) {
                mSortItem.setVisible(mPresenter.isSortMenuVisible());
            }
            if (mFilterItem != null) {
                mFilterItem.setVisible(mPresenter.isFilterMenuVisible());
            }
        }
    }

    @Override
    public void onPresenterCreated(PresenterInterface presenter) {
        mPresenter = presenter;
        setVisibleMenuItems();
    }

    private void showDeputyDetails(Deputy deputy){
        FrameLayout fl = (FrameLayout) findViewById(R.id.frame_searchdetails);
        if (fl != null){
            DeputyDetailsFragment fragment = DeputyDetailsFragment.newInstance(deputy);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchdetails, fragment).commit();
        } else {
            Intent activityIntent = new Intent(this, DeputyDetailsActivity.class);
            activityIntent.putExtra(KEY_DEPUTY, deputy);
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
        Intent activityIntent = new Intent(this, LawDetailsActivity.class);
        activityIntent.putExtra(KEY_LAW, law);
        startActivity(activityIntent);
    }
}
