package ru.merkulyevsasha.gosduma.mvp.deputies;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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
import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.laws.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsPresenter;
import ru.merkulyevsasha.gosduma.mvp.laws.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

public class DeputyDetailsActivity extends BaseActivity
    implements ViewInterface,
    ViewInterface.OnLawClickListener
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

        if (isLargeLandscape()) {
            finish();
            return;
        }

        mPresenter = new LawsPresenter(this, this);

        setContentView(R.layout.activity_deputydetails);
        ButterKnife.bind(this);

        initToolbar(R.id.deputydetails_toolbar);

        Intent intent = getIntent();
        mDeputy = intent.getParcelableExtra("deputy");
        setTitle("");

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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
        });

        mDeputyPosition.setText(mDeputy.getPositionWithStartAndEndDates());

        mDeputyName.setText(mDeputy.getNameWithBirthday());

        setText(mDeputy.getRanksWithDegrees(), mDeputyRanks);

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

                message.append(mDeputy.getNameWithBirthday());
                message.append("\n");
                message.append(mDeputy.getPositionWithStartAndEndDates());
                message.append("\n");
                if (!mDeputy.getRanksWithDegrees().isEmpty()) {
                    message.append(mDeputy.getRanksWithDegrees());
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
    public void show(List<Deputy> items) {

    }

    @Override
    public void onLawClick(Law law) {
        Intent activityIntent = new Intent(this, LawDetailsActivity.class);
        activityIntent.putExtra("law", law);
        startActivity(activityIntent);
    }

}
