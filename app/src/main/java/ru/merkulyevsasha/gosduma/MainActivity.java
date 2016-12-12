package ru.merkulyevsasha.gosduma;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener{

    private final int IDD_SORT = 1;
    private final int IDD_FILTER = 2;

    private AlertDialog.Builder mBuilder;
    private int mSortItem = -1;

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

        switch (id) {
            case IDD_SORT:

                final String[] sortItems ={"По наименованию", "По номеру", "По дате"};

                mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle("Сортировка");
                mBuilder.setCancelable(false);

                mBuilder.setSingleChoiceItems(sortItems, mSortItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            mSortItem = item;
                            Toast.makeText(getApplicationContext(), "Сортировка: " + sortItems[item], Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                return mBuilder.create();

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
                showDialog(IDD_FILTER);
                return false;
            }
        });

        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showDialog(IDD_SORT);
                return false;
            }
        });

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }

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

    private void showNavFragment(int id, String name){

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
