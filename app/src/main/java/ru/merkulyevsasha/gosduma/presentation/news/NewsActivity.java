package ru.merkulyevsasha.gosduma.presentation.news;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import ru.merkulyevsasha.gosduma.presentation.main.MainActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.helpers.AdRequestHelper;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;

public class NewsActivity extends AppCompatActivity implements NewsView{

    @BindView(R.id.news_toolbar) Toolbar toolbar;
    @BindView(R.id.root) View root;
    @BindView(R.id.swipe_listviewnews) SwipeRefreshLayout refreshLayout;
    @BindView(R.id.listview_news) ListView listView;
    @BindView(R.id.adView) AdView adView;

    private ListViewNewsAdapter adapter;

    private int mId = -1;
    private String mName = "";
    private boolean notificationStart;

    private AdRequest adRequest;
    private InterstitialAd interstitialAd;

    @Inject NewsPresenter pres;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        mId = intent.getIntExtra(KeysBundleHolder.KEY_ID, 0);
        mName = intent.getStringExtra(KeysBundleHolder.KEY_NAME);
        notificationStart = intent.getBooleanExtra(KeysBundleHolder.KEY_NOTIFICATION, false);
        setTitle(mName);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                pres.refresh(mId);
            }
        });

        adapter = new ListViewNewsAdapter(this, new ArrayList<Article>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pres.onItemClicked(adapter.getItem(position), interstitialAd);
            }
        });

        adRequest = AdRequestHelper.getAdRequest();
        adView.loadAd(adRequest);

        // Create the InterstitialAd and set the adUnitId.
        interstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
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
        if (interstitialAd != null && !interstitialAd.isLoaded() && !interstitialAd.isLoading()) {
            interstitialAd.loadAd(adRequest);
        }

        pres.bind(this);
        pres.load(mId);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        pres.onDestroy();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        Snackbar.make(root, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void showNews(List<Article> articles) {
        adapter.setItems(articles);
    }

    @Override
    public void showDetailsItem(Article item) {
        FrameLayout framelayout = findViewById(R.id.frame_newsdetails);
        if (framelayout != null){
            NewsDetailsFragment fragment = NewsDetailsFragment.newInstance(item.Title, item.Description);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_newsdetails, fragment).commit();
        } else {
            NewsDetailsActivity.startScreen(this, mName, item);
        }
    }

    @Override
    public void onBackPressed() {
        if (notificationStart){
            Intent intent =  new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private class ListViewNewsAdapter extends ArrayAdapter<Article> {

        private final List<Article> items;
        private final LayoutInflater inflater;

        ListViewNewsAdapter(Context context, List<Article> items) {
            super(context, R.layout.row_listdata_item);

            this.items = items;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void clear() {
            super.clear();
            items.clear();
        }

        @Override
        public void addAll(@NonNull Collection<? extends Article> collection) {
            super.addAll(collection);
            items.addAll(collection);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            if (convertView == null){
                convertView = inflater.inflate(R.layout.row_listdata_item, parent, false);
                convertView.setTag(convertView.findViewById(R.id.textview_topic));
            }

            TextView textViewTopic = (TextView) convertView.getTag();

            Article item = items.get(position);

            DateFormat format = SimpleDateFormat.getDateInstance();
            textViewTopic.setText(String.format("%s %s", format.format(item.PubDate), item.Title));

            return convertView;
        }

        public void setItems(List<Article> articles) {
            clear();
            addAll(articles);
            notifyDataSetChanged();
        }
    }


}
