package ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;


public class DeputyRequestDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_deputyrequest_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        final DeputyRequest deputyRequest = intent.getParcelableExtra(KeysBundleHolder.KEY_DEPUTYREQUEST);
        if (deputyRequest == null) {
            finish();
            return;
        }

        Fragment fragment = DeputyRequestDetailsFragment.newInstance(deputyRequest);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();

        setTitle(R.string.menu_deputies_requests);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(deputyRequest);
            }
        });

    }

    private void share(DeputyRequest deputyRequest) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        final StringBuilder message = new StringBuilder();

        message.append(deputyRequest.getNameWithNumberAndDate());
        message.append("\n");
        if (deputyRequest.initiator!= null && !deputyRequest.initiator.isEmpty()){
            message.append(getString(R.string.text_initiator));
            message.append(deputyRequest.initiator);
            message.append("\n");
        }
        if (deputyRequest.answer!= null && !deputyRequest.answer.isEmpty()){
            message.append(getString(R.string.text_answer));
            message.append(deputyRequest.answer);
            message.append("\n");
        }
        if (deputyRequest.resolution!= null && !deputyRequest.resolution.isEmpty()){
            message.append(getString(R.string.text_resolution));
            message.append(deputyRequest.resolution);
            message.append("\n");
        }

        sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
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

    public static void startScreen(Context context, DeputyRequest deputyRequest) {
        Intent activityIntent = new Intent(context, DeputyRequestDetailsActivity.class);
        activityIntent.putExtra(KeysBundleHolder.KEY_DEPUTYREQUEST, deputyRequest);
        context.startActivity(activityIntent);
    }
}
