package ru.merkulyevsasha.gosduma.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.http.RssParser;
import ru.merkulyevsasha.gosduma.http.RssService;
import ru.merkulyevsasha.gosduma.models.Article;

import static ru.merkulyevsasha.gosduma.MainActivity.KEY_ID;
import static ru.merkulyevsasha.gosduma.MainActivity.KEY_NAME;
import static ru.merkulyevsasha.gosduma.mvp.deputies.DeputyDetailsActivity.KEY_POSITION;

public class NewsActivity extends BaseActivity
        implements AdapterView.OnItemClickListener{

    public final static String KEY_TOPIC = "TOPIC";
    public final static String KEY_DESCRIPTION = "DESCRIPTION";

    private ListViewNewsAdapter mAdapter;

    private SwipeRefreshLayout mRefreshLayout;

    private int mId = -1;
    private String mName = "";
    private int mPosition = -1;
    private DatabaseHelper mDatabase;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, mPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initSupportActionBarWithBackButton(R.id.news_toolbar);

        Intent intent = getIntent();
        mId = intent.getIntExtra(KEY_ID, 0);
        mName = intent.getStringExtra(KEY_NAME);
        setTitle(mName);

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

        mDatabase = DatabaseHelper.getInstance(DatabaseHelper.getDbPath(this));

        List<Article> articles = mDatabase.getArticles(mId);
        if (articles.size() > 0){
            mAdapter.clear();
            mAdapter.addAll (articles);
            mAdapter.notifyDataSetChanged();

            if (savedInstanceState != null){
                mPosition = savedInstanceState.getInt(KEY_POSITION, -1);
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

        showDetailsOnPosition(i);
    }

}
