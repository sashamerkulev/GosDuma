package ru.merkulyevsasha.gosduma.presentation.listdata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;


public class ListDataActivity extends AppCompatActivity implements ListDataView {

    @BindView(R.id.list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.listview_listdata)
    ListView listView;
    @BindView(R.id.adView)
    AdView adView;

    @Inject
    ListDataPresenter pres;

    private final HashMap<Integer, String> listDataTableName = new HashMap<>();

    private int menuId;
    private ListViewListDataAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        GosDumaApp.getComponent().inject(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        menuId = intent.getIntExtra(KeysBundleHolder.KEY_ID, 0);
        final String name = intent.getStringExtra(KeysBundleHolder.KEY_NAME);
        setTitle(name);

        listDataTableName.put(R.id.nav_comittee, DatabaseHelper.COMMITTEE_TABLE_NAME);
        listDataTableName.put(R.id.nav_blocks, DatabaseHelper.BLOCKS_TABLE_NAME);
        listDataTableName.put(R.id.nav_otras, DatabaseHelper.OTRAS_TABLE_NAME);
        listDataTableName.put(R.id.nav_reg, DatabaseHelper.REG_TABLE_NAME);
        listDataTableName.put(R.id.nav_fed, DatabaseHelper.FED_TABLE_NAME);
        listDataTableName.put(R.id.nav_stad, DatabaseHelper.STAD_TABLE_NAME);
        listDataTableName.put(R.id.nav_inst, DatabaseHelper.INST_TABLE_NAME);

        adapter = new ListViewListDataAdapter(this, new ArrayList<ListData>());
        listView.setAdapter(adapter);

        AdRequest adRequest = AdRequestHelper.getAdRequest();
        adView.loadAd(adRequest);

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
        pres.load(listDataTableName.get(menuId));
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void showMessage(@StringRes int resId) {

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
                adapter.setItems(items);
            }
        });
    }

    private class ListViewListDataAdapter extends ArrayAdapter<ListData> {

        private final List<ListData> mItems;
        private final LayoutInflater mInflater;

        ListViewListDataAdapter(Context context, List<ListData> items) {
            super(context, R.layout.listview_newsitem, items);

            mItems = items;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public void addAll(@NonNull Collection<? extends ListData> collection) {
            super.addAll(collection);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_newsitem, parent, false);
                convertView.setTag(convertView.findViewById(R.id.textview_topic));
            }
            TextView textViewTopic = (TextView) convertView.getTag();

            ListData item = mItems.get(position);

            textViewTopic.setText(item.name);

            return convertView;
        }

        public void setItems(List<ListData> items) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

}