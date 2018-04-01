package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.ToolbarCombinator;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;


public class DeputyDetailsActivity extends AppCompatActivity
        implements HasSupportFragmentInjector, ToolbarCombinator {

    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_deputy_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final Deputy deputy = intent.getParcelableExtra(KeysBundleHolder.KEY_DEPUTY);
        if (deputy == null) {
            finish();
            return;
        }

        DeputyDetailsFragment fragment = DeputyDetailsFragment.newInstance(deputy, false);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(deputy);
            }
        });
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

    public void share(Deputy deputy) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        final StringBuilder message = new StringBuilder();

        message.append(deputy.getNameWithBirthday());
        message.append("\n");
        message.append(deputy.getPositionWithStartAndEndDates());
        message.append("\n");
        if (!deputy.getRanksWithDegrees().isEmpty()) {
            message.append(deputy.getRanksWithDegrees());
            message.append("\n");
        }
        message.append(deputy.fractionName);
        message.append("\n");
        message.append(deputy.fractionRole);
        if (!deputy.fractionRegion.isEmpty()){
            message.append(" ");
            message.append(deputy.fractionRegion);
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
    }

    public static void startScreen(Context context, Deputy deputy) {
        Intent activityIntent = new Intent(context, DeputyDetailsActivity.class);
        activityIntent.putExtra(KeysBundleHolder.KEY_DEPUTY, deputy);
        context.startActivity(activityIntent);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void connectToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
