package ru.merkulyevsasha.gosduma.presentation.listdata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.BuildConfig;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;


public class ListDataActivity extends AppCompatActivity implements ListDataView {

    private final HashMap<Integer, String> mListDataTableName = new HashMap<>();

    private ListViewListDataAdapter mAdapter;

    @Inject
    ListDataPresenter presenter;

    private int menuId;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        GosDumaApp.getComponent().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        menuId = intent.getIntExtra(KeysBundleHolder.KEY_ID, 0);
        final String name = intent.getStringExtra(KeysBundleHolder.KEY_NAME);
        setTitle(name);

        mListDataTableName.put(R.id.nav_comittee, DatabaseHelper.COMMITTEE_TABLE_NAME);
        mListDataTableName.put(R.id.nav_blocks, DatabaseHelper.BLOCKS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_otras, DatabaseHelper.OTRAS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_reg, DatabaseHelper.REG_TABLE_NAME);
        mListDataTableName.put(R.id.nav_fed, DatabaseHelper.FED_TABLE_NAME);
        mListDataTableName.put(R.id.nav_stad, DatabaseHelper.STAD_TABLE_NAME);
        mListDataTableName.put(R.id.nav_inst, DatabaseHelper.INST_TABLE_NAME);

        ListView mListView = (ListView) findViewById(R.id.listview_listdata);
        mAdapter = new ListViewListDataAdapter(this, new ArrayList<ListData>());
        mListView.setAdapter(mAdapter);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = AdRequestHelper.getAdRequest();
        mAdView.loadAd(adRequest);

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
    protected void onStop() {
        super.onStop();
        if (presenter != null){
            presenter.onStop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (presenter != null){
            presenter.onStart(this);
            presenter.load(mListDataTableName.get(menuId));
        }
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showList(final List<ListData> items) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                mAdapter.addAll(items);
                mAdapter.notifyDataSetChanged();
            }
        });
   }
}