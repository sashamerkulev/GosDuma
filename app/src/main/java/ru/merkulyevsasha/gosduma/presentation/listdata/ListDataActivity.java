package ru.merkulyevsasha.gosduma.presentation.listdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;


public class ListDataActivity extends AppCompatActivity implements ListDataView {

    @BindView(R.id.root) View root;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.listview_listdata) ListView listView;
    @BindView(R.id.progressbar) ProgressBar progressbar;

    @BindView(R.id.adView) AdView adView;

    @Inject ListDataPresenter pres;

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, String> listDataTableName = new HashMap<>();

    private int menuId;
    private ListViewListDataAdapter adapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        unbinder = ButterKnife.bind(this);

        AndroidInjection.inject(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
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
        pres.unbind();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        pres.bind(this);
        pres.load(listDataTableName.get(menuId));
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        unbinder.unbind();
        pres.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        Snackbar.make(root, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showList(final List<ListData> items) {
        adapter.setItems(items);
    }

    private class ListViewListDataAdapter extends ArrayAdapter<ListData> {

        private final List<ListData> mItems;
        private final LayoutInflater mInflater;

        ListViewListDataAdapter(Context context, List<ListData> items) {
            super(context, R.layout.row_listdata_item, items);

            mItems = items;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row_listdata_item, parent, false);
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