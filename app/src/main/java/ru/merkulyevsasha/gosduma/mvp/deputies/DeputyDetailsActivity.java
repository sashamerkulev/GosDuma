package ru.merkulyevsasha.gosduma.mvp.deputies;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsPresenter;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

public class DeputyDetailsActivity extends AppCompatActivity
    implements ViewInterface,
    ViewInterface.OnLawClickListener,
        AppBarLayout.OnOffsetChangedListener
{

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.appbarlayout)
    AppBarLayout mAppbarLayout;

    @BindView(R.id.textview_deputy_name)
    TextView mDeputyName;
    @BindView(R.id.textview_position)
    TextView mDeputyPosition;
    @BindView(R.id.textview_deputy_fractionName)
    TextView mFractionName;
    @BindView(R.id.textview_deputy_fractionRole)
    TextView mFractionRole;
    @BindView(R.id.textview_deputy_ranks)
    TextView mDeputyRanks;

    @BindView(R.id.fab)
    public FloatingActionButton mFab;

    @BindView(R.id.recyclerview_deputy_laws)
    public RecyclerView mRecyclerView;

    private LawsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

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
        //setTitle(mDeputy.name);
        setTitle("");

        mAppbarLayout.addOnOffsetChangedListener(this);



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
        mDeputyPosition.setText(position.toString());

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

        List<Law> items = mPresenter.getDeputyLaws(mDeputy.id);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LawsRecyclerViewAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);

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

    @Override
    public void onLawClick(Law law) {
        Snackbar.make(this.findViewById(R.id.details_content), law.name, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
            // Collapsed
            setTitle(mDeputy.name);
            mCollapsingToolbar.setTitleEnabled(true);
            mDeputyName.setTextColor(Color.TRANSPARENT);
            mDeputyPosition.setTextColor(Color.TRANSPARENT);
            mDeputyRanks.setTextColor(Color.TRANSPARENT);
            mFractionName.setTextColor(Color.TRANSPARENT);
            mFractionRole.setTextColor(Color.TRANSPARENT);
        } else if (verticalOffset == 0) {
            // Expanded
            setTitle("");
            mCollapsingToolbar.setTitleEnabled(false);
            mDeputyName.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mDeputyPosition.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mDeputyRanks.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mFractionName.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mFractionRole.setTextColor(getResources().getColor(R.color.textColorPrimary));
        } else {
            // Somewhere in between
            setTitle("");
            mCollapsingToolbar.setTitleEnabled(false);
            mDeputyName.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mDeputyPosition.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mDeputyRanks.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mFractionName.setTextColor(getResources().getColor(R.color.textColorPrimary));
            mFractionRole.setTextColor(getResources().getColor(R.color.textColorPrimary));
        }
    }
}
