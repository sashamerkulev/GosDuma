package ru.merkulyevsasha.gosduma;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.http.RssParser;
import ru.merkulyevsasha.gosduma.http.RssService;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.ui.ListViewNewsAdapter;

public class NewsActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener{

    private ListViewNewsAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private int mId = -1;
    private int mPosition = -1;
    private DatabaseHelper mDatabase;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mId = intent.getIntExtra("id", 0);
        final String name = intent.getStringExtra("name");
        setTitle(name);

        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_listviewnews);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                updateNewsData(mId);
            }
        });

        ListView mListView = (ListView) findViewById(R.id.listview_news);
        mAdapter = new ListViewNewsAdapter(this, new ArrayList<Article>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        mDatabase = DatabaseHelper.getInstance(this);

        List<Article> articles = mDatabase.getArticles(mId);
        if (articles.size() > 0){
            mAdapter.clear();
            mAdapter.addAll (articles);
            mAdapter.notifyDataSetChanged();

            if (savedInstanceState != null){
                mPosition = savedInstanceState.getInt("position", -1);
                if (mPosition > 0) {
                    showDetailsOnPosition(mPosition);
                }
            }

        } else {
            mRefreshLayout.setRefreshing(true);
            updateNewsData(mId);
        }

    }

    private void updateNewsData(int id){
        if (id == R.id.nav_news_gd){
            RssService service = RssService.getInstance();
            service.gosduma().enqueue(getSubscriber());
        } else if (id == R.id.nav_news_preds){
            RssService service = RssService.getInstance();
            service.chairman().enqueue(getSubscriber());
        }
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


    private Callback<ResponseBody> getSubscriber() {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {

                        RssParser parser = new RssParser();

                        List<Article> result = parser.parseXml(response.body().string());

                        mDatabase.deleteArticles(mId);
                        mDatabase.addArticles(mId, result);

                        mAdapter.clear();
                        mAdapter.addAll (result);
                        mAdapter.notifyDataSetChanged();
                        mRefreshLayout.setRefreshing(false);
                    }
                    catch(Exception e){
                        mRefreshLayout.setRefreshing(false);
                        FirebaseCrash.report(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.report(t);
            }
        };

    }

    private void showDetailsOnPosition(int position){
        Article item = mAdapter.getItem(position);
        if (item != null) {

            mPosition = position;

            FrameLayout fl = (FrameLayout) findViewById(R.id.frame_newsdetails);
            if (fl != null){
                NewsDetailsFragment fragment = NewsDetailsFragment.newInstance(item.Title, item.Description);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_newsdetails, fragment).commit();
            } else {
                Intent activityIntent = new Intent(this, NewsDetailsActivity.class);
                activityIntent.putExtra("topic", item.Title);
                activityIntent.putExtra("description", item.Description);
                startActivity(activityIntent);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showDetailsOnPosition(i);
    }
}
