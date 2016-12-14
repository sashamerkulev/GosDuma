package ru.merkulyevsasha.gosduma;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.mvp.DeputiesFragment;
import ru.merkulyevsasha.gosduma.mvp.DeputiesPresenter;
import ru.merkulyevsasha.gosduma.mvp.PresenterInterface;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        ViewInterface.OnViewListener,
        ViewInterface.onDeputyClickListener
{

    public final static int IDD_DEPUTY_SORT = 1;
    public final static int IDD_DEPUTY_FILTER = 2;

    private PresenterInterface mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .show();
//            }
//        });

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
            setDeputyFragment();
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
    protected Dialog onCreateDialog(int id) {

        AlertDialog.Builder builder;

        switch (id) {
            case IDD_DEPUTY_SORT:

                final String[] sortItems = {getString(R.string.item_sort_name), getString(R.string.item_sort_birtdate),
                        getString(R.string.item_sort_fractionname)};

                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.title_sort);

                builder.setSingleChoiceItems(sortItems, mPresenter.getCurrentSortIndexValue().get(0),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {

                                List<Integer> newSort = new ArrayList<Integer>();
                                newSort.add(item);

                                mPresenter.sort(mPresenter.getCurrentSortIndexValue(), newSort);
                                dialog.dismiss();
                            }
                        });

                return builder.create();

            case IDD_DEPUTY_FILTER:

                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.title_filter);

                View view = getLayoutInflater().inflate(R.layout.dialog_deputy_filter, null); // Получаем layout по его ID
                builder.setView(view);
                final RadioButton rb_deputy = (RadioButton)view.findViewById(R.id.radiobox_deputy_gd);
                final RadioButton rb_member = (RadioButton)view.findViewById(R.id.radiobox_member);
                final RadioButton rb_working = (RadioButton)view.findViewById(R.id.radiobox_working);
                final RadioButton rb_not_working = (RadioButton)view.findViewById(R.id.radiobox_not_working);

                final List<Integer> filterSettings = mPresenter.getCurrentFilterIndexValue();
                final int deputy =  filterSettings.get(0);

                rb_deputy.setChecked(deputy == DeputiesPresenter.DEPUTY_INDEX);
                rb_member.setChecked(deputy == DeputiesPresenter.MEMBER_INDEX);

                final int working =  filterSettings.get(1);
                rb_working.setChecked(working == DeputiesPresenter.WORKING_INDEX);
                rb_not_working.setChecked(working == DeputiesPresenter.NOT_WORKING_INDEX);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    public void onClick(DialogInterface dialog, int whichButton) {
                        List<Integer> newFilterSettings = new ArrayList<Integer>();
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
                        mPresenter.filter(newFilterSettings);
                    }
                });

                return builder.create();


            default:
                return null;
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        MenuItem filterItem = menu.findItem(R.id.action_filter);
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showDialog(mPresenter.getFilterDialogType());
                return false;
            }
        });

        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showDialog(mPresenter.getSortDialogType());
                return false;
            }
        });

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
            showNavFragment(R.id.nav_deputies, item.getTitle().toString());
        } else if (id == R.id.nav_laws) {
            showNavFragment(R.id.nav_laws, item.getTitle().toString());
        } else if (id == R.id.nav_depqueries) {
            showNavFragment(R.id.nav_depqueries, item.getTitle().toString());
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startActivity(int id, String name, Class<?> cls){
        Intent activityIntent = new Intent(this, cls);
        activityIntent.putExtra("id", id);
        activityIntent.putExtra("name", name);
        startActivity(activityIntent);
    }

    private void startNewsActivity(int id, String name){
        startActivity(id, name, NewsActivity.class);
    }

    private void startListActivity(int id, String name){
        startActivity(id, name, ListActivity.class);
    }

    private void setDeputyFragment(){
        setTitle(R.string.menu_deputies);
        DeputiesFragment fragment = new DeputiesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_searchlist, fragment).commit();
    }

    private void showNavFragment(int id, String name){

        if (id == R.id.nav_deputies) {
            setDeputyFragment();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onPresenterCreated(PresenterInterface presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDeputyClick(Deputy deputy) {
        Snackbar.make(findViewById(R.id.main_content_layout), deputy.name, Snackbar.LENGTH_LONG).show();
    }
}
