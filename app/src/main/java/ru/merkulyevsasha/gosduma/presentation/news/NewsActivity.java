package ru.merkulyevsasha.gosduma.presentation.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.BuildConfig;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Article;

import static ru.merkulyevsasha.gosduma.MainActivity.KEY_ID;
import static ru.merkulyevsasha.gosduma.MainActivity.KEY_NAME;
import static ru.merkulyevsasha.gosduma.presentation.deputydetails.DeputyDetailsActivity.KEY_POSITION;

public class NewsActivity extends BaseActivity
        implements NewsView, AdapterView.OnItemClickListener{

    public final static String KEY_TOPIC = "TOPIC";
    public final static String KEY_DESCRIPTION = "DESCRIPTION";

    private ListViewNewsAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;
    private View root;

    private int mId = -1;
    private String mName = "";
    private int mPosition = -1;

    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Inject
    NewsPresenter presenter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, mPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        GosDumaApp.getComponent().inject(this);

        initSupportActionBarWithBackButton(R.id.news_toolbar);

        Intent intent = getIntent();
        mId = intent.getIntExtra(KEY_ID, 0);
        mName = intent.getStringExtra(KEY_NAME);
        setTitle(mName);

        root = findViewById(R.id.root);

        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_listviewnews);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                presenter.refresh(mId);
            }
        });

        ListView mListView = (ListView) findViewById(R.id.listview_news);
        mAdapter = new ListViewNewsAdapter(this, new ArrayList<Article>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = BuildConfig.DEBUG_MODE
                ? new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
                : new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.app_unit_id));

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showDetailsOnPosition(mPosition);
            }
        });
    }

    private void showDetailsOnPosition(int position){
        Article item = mAdapter.getItem(position);
        if (item != null) {
            FrameLayout fl = (FrameLayout) findViewById(R.id.frame_newsdetails);
            if (fl != null){
                NewsDetailsFragment fragment = NewsDetailsFragment.newInstance(item.Title, item.Description);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_newsdetails, fragment).commit();
            } else {
                mPosition = -1;
                Intent activityIntent = new Intent(this, NewsDetailsActivity.class);
                activityIntent.putExtra(KEY_TOPIC, item.Title);
                activityIntent.putExtra(KEY_DESCRIPTION, item.Description);
                activityIntent.putExtra(KEY_NAME, mName);
                startActivity(activityIntent);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mPosition = i;
        //showDetailsOnPosition(i);
        showInterstitial();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            showDetailsOnPosition(mPosition);
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
        mInterstitialAd.loadAd(adRequest);
        if (presenter != null){
            presenter.onStart(this);
            presenter.load(mId);
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
    public void showMessage(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(root, resId, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void showNews(final List<Article> articles) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                mAdapter.addAll(articles);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
