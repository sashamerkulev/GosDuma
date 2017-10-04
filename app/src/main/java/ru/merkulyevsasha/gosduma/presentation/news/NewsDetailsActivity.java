package ru.merkulyevsasha.gosduma.presentation.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;


public class NewsDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_newsdetails);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        final String topic = intent.getStringExtra(KeysBundleHolder.KEY_TOPIC);
        final String description = intent.getStringExtra(KeysBundleHolder.KEY_DESCRIPTION);
        final String name = intent.getStringExtra(KeysBundleHolder.KEY_NAME);
        setTitle(name);

        Fragment fragment = NewsDetailsFragment.newInstance(topic, description);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();

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

    public static void startScreen(Context context, String name, Article item){
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(KeysBundleHolder.KEY_TOPIC, item.Title);
        intent.putExtra(KeysBundleHolder.KEY_DESCRIPTION, item.Description);
        intent.putExtra(KeysBundleHolder.KEY_NAME, name);
        context.startActivity(intent);
    }
}
