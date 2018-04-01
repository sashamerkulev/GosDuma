package ru.merkulyevsasha.gosduma.presentation.lawdetails;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Law;

import ru.merkulyevsasha.gosduma.helpers.UiUtils;


public class LawDetailsActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public final static String KEY_LAW = "LAW";
    public final static String KEY_DEPUTY_LAW = "DEPUTY_LAW";

    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;
    @BindView(R.id.fab) FloatingActionButton fab;

    private Law law;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawdetails);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        boolean deputyDetails = intent.getBooleanExtra(LawDetailsActivity.KEY_DEPUTY_LAW, false);


        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (UiUtils.isLargeLandscape(this) && !deputyDetails) {
            finish();
            return;
        }

        setTitle(R.string.menu_laws);

        law = intent.getParcelableExtra(KEY_LAW);
        if (law == null) {
            finish();
            return;
        }

        Fragment fragment = LawDetailsFragment.newInstance(law);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
        .commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(law);
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

    public void share(Law law) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        final StringBuilder message = new StringBuilder();

        message.append(law.type);
        message.append("\n");
        message.append(law.getLawNameWithNumberAndDate());
        message.append("\n");
        if (law.comments != null && !law.comments.isEmpty()) {
            message.append(getResources().getString(R.string.text_comment));
            message.append(law.comments);
            message.append("\n");
        }
        if (law.lastEventSolution != null && !law.lastEventSolution.isEmpty()){
            message.append(getResources().getString(R.string.text_solution));
            message.append(law.lastEventSolution);
            message.append("\n");
        }
        if (law.responsibleName != null && !law.responsibleName.isEmpty()){
            message.append(getResources().getString(R.string.text_resp_comittee));
            message.append(law.responsibleName);
            message.append("\n");
        }
        sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
    }

    public static void startScreen(Context context, Law law, boolean deputy) {
        Intent activityIntent = new Intent(context, LawDetailsActivity.class);
        activityIntent.putExtra(LawDetailsActivity.KEY_LAW, law);
        activityIntent.putExtra(LawDetailsActivity.KEY_DEPUTY_LAW, deputy);
        context.startActivity(activityIntent);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }
}
