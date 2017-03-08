package ru.merkulyevsasha.gosduma.presentation.listdata;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.BuildConfig;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.ListData;

import static ru.merkulyevsasha.gosduma.MainActivity.KEY_ID;
import static ru.merkulyevsasha.gosduma.MainActivity.KEY_NAME;


public class ListDataActivity extends BaseActivity {

    private final HashMap<Integer, String> mListDataTableName = new HashMap<>();

    @Inject
    public DatabaseHelper mDatabase;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        GosDumaApp.getComponent().inject(this);

        initSupportActionBarWithBackButton(R.id.list_toolbar);

        Intent intent = getIntent();
        final int id = intent.getIntExtra(KEY_ID, 0);
        final String name = intent.getStringExtra(KEY_NAME);
        setTitle(name);

        mListDataTableName.put(R.id.nav_comittee, DatabaseHelper.COMMITTEE_TABLE_NAME);
        mListDataTableName.put(R.id.nav_blocks, DatabaseHelper.BLOCKS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_otras, DatabaseHelper.OTRAS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_reg, DatabaseHelper.REG_TABLE_NAME);
        mListDataTableName.put(R.id.nav_fed, DatabaseHelper.FED_TABLE_NAME);
        mListDataTableName.put(R.id.nav_stad, DatabaseHelper.STAD_TABLE_NAME);
        mListDataTableName.put(R.id.nav_inst, DatabaseHelper.INST_TABLE_NAME);

        List<ListData> data = mDatabase.selectAll(mListDataTableName.get(id));

        ListView mListView = (ListView) findViewById(R.id.listview_listdata);
        ListViewListDataAdapter mAdapter = new ListViewListDataAdapter(this, new ArrayList<ListData>());
        mListView.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = BuildConfig.DEBUG_MODE
                ? new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
                : new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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

}