package ru.merkulyevsasha.gosduma;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.mvp.LawsPresenter;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

public class DeputyDetailsActivity extends AppCompatActivity
    implements ViewInterface{

    @BindView(R.id.textview_deputy_name)
    TextView mDeputyName;
    @BindView(R.id.textview_position)
    TextView mPosition;
    @BindView(R.id.textview_deputy_fractionName)
    TextView mFractionName;
    @BindView(R.id.textview_deputy_fractionRole)
    TextView mFractionRole;
    @BindView(R.id.textview_deputy_ranks)
    TextView mDeputyRanks;

    @BindView(R.id.fab)
    public FloatingActionButton mFab;

    private Deputy mDeputy;
    private LawsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        mPresenter = new LawsPresenter(this, this);

        setContentView(R.layout.activity_deputydetails);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.deputydetails_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        mDeputy = intent.getParcelableExtra("deputy");
        setTitle(mDeputy.name);

        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (mDeputy.credentialsStart > 0) {

            String period = " (период с " + format.format(new Date(mDeputy.credentialsStart));
            if (mDeputy.credentialsEnd > 0) {
                period = period + " по " + format.format(new Date(mDeputy.credentialsStart));
            }
            period = period +")";

            mPosition.setText(mDeputy.position + period);
        } else{
            mPosition.setText(mDeputy.position);
        }

        if (mDeputy.birthdate > 0) {
            mDeputyName.setText(mDeputy.name + " (" + format.format(new Date(mDeputy.birthdate)) + ")");
        } else {
            mDeputyName.setText(mDeputy.name);
        }

        if (mDeputy.degrees.isEmpty()){
            mDeputyRanks.setVisibility(View.GONE);
        } else {
            String ranks = mDeputy.ranks.isEmpty()? mDeputy.degrees : mDeputy.degrees + " (" + mDeputy.ranks + ")";
            mDeputyRanks.setText(ranks);
        }

        mFractionName.setText(mDeputy.fractionName);
        mFractionRole.setText(mDeputy.fractionRole + " " + mDeputy.fractionRegion);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mDeputy.name);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
            }
        });

        mPresenter.getDeputyLaws(mDeputy.id);

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
    public void show(List<Deputy> items) {

    }
}
