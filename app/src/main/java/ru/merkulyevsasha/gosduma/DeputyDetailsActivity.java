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

        final StringBuilder position = new StringBuilder();
        final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (mDeputy.credentialsStart > 0) {
            position.append(mDeputy.position);
            position.append(" (период с ");
            position.append(format.format(new Date(mDeputy.credentialsStart)));
            if (mDeputy.credentialsEnd > 0) {
                position.append(" по " + format.format(new Date(mDeputy.credentialsStart)));
            }
            position.append(")");
        } else{
            position.append(mDeputy.position);
        }
        mPosition.setText(position.toString());

        final StringBuilder name = new StringBuilder();
        if (mDeputy.birthdate > 0) {
            name.append(mDeputy.name);
            name.append(" (" + format.format(new Date(mDeputy.birthdate)) + ")");
        } else {
            name.append(mDeputy.name);
        }
        mDeputyName.setText(name.toString());

        final StringBuilder ranks = new StringBuilder();
        if (mDeputy.degrees.isEmpty()){
            mDeputyRanks.setVisibility(View.GONE);
        } else {
            ranks.append(mDeputy.ranks.isEmpty()? mDeputy.degrees : mDeputy.degrees + " (" + mDeputy.ranks + ")");
            mDeputyRanks.setText(ranks);
        }

        mFractionName.setText(mDeputy.fractionName);
        mFractionRole.setText(mDeputy.fractionRole + " " + mDeputy.fractionRegion);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                final StringBuilder message = new StringBuilder();

                message.append(name.toString());
                message.append("\n");
                message.append(position.toString());
                message.append("\n");
                if (ranks.length() > 0) {
                    message.append(ranks.toString());
                    message.append("\n");
                }
                message.append(mDeputy.fractionName);
                message.append("\n");
                message.append(mDeputy.fractionRole);
                if (!mDeputy.fractionRegion.isEmpty()){
                    message.append(" ");
                    message.append(mDeputy.fractionRegion);
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

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
