package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractorImpl;
import ru.merkulyevsasha.gosduma.models.Law;

import static ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity.KEY_LAW;
import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrLayoutGone;

public class DeputyLawDetailsActivity extends AppCompatActivity implements LawDetailsView  {


    @BindView(R.id.tv_law_type)
    public TextView mLawType;

    @BindView(R.id.tv_law_name)
    public TextView mLawName;

    @BindView(R.id.tv_law_comments)
    public TextView mLawComments;

    @BindView(R.id.tv_law_responsible)
    public TextView mLawResponsible;

    @BindView(R.id.tv_law_solution)
    public TextView mLawSolution;

    @BindView(R.id.tv_law_stage)
    public TextView mLawStage;

    @BindView(R.id.tv_law_phase)
    public TextView mLawPhase;

    @BindView(R.id.tv_law_coexeccomittees)
    public TextView mLawCoexecComittees;

    @BindView(R.id.tv_law_profilecomittees)
    public TextView mLawProgileComittees;

    @BindView(R.id.tv_law_deputies)
    public TextView mLawDeputies;

    @BindView(R.id.tv_law_departments)
    public TextView mLawDepartments;


    @BindView(R.id.layout_comments)
    public LinearLayout mLayoutComments;

    @BindView(R.id.layout_phase)
    public LinearLayout mLayoutPhase;

    @BindView(R.id.layout_stage)
    public LinearLayout mLayoutStage;

    @BindView(R.id.layout_responsible)
    public LinearLayout mLayoutResponsible;

    @BindView(R.id.layout_solution)
    public LinearLayout mLayoutSolution;

    @BindView(R.id.layout_coexeccomittees)
    public LinearLayout mLayoutCoexecComittees;

    @BindView(R.id.layout_profilecomittees)
    public LinearLayout mLayoutProfileComittees;

    @BindView(R.id.layout_deputies)
    public LinearLayout mLayoutDeputies;

    @BindView(R.id.layout_departments)
    public LinearLayout mLayoutDepartments;

    @BindView(R.id.fab)
    public FloatingActionButton mFab;

    @BindView(R.id.progressBar)
    public ProgressBar mProgressBar;

    private String mStage;
    private String mPhase;
    private String mProfile;
    private String mCoexec;
    private String mDdeputies;
    private String mDepartments;

    private Law mLaw;

    @Inject
    public LawDetailsPresenter mPresenter;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAW, mLaw);
        outState.putString(LawDetailsInteractorImpl.KEY_STAGE, mStage);
        outState.putString(LawDetailsInteractorImpl.KEY_PHASE, mPhase);
        outState.putString(LawDetailsInteractorImpl.KEY_PROFILE, mProfile);
        outState.putString(LawDetailsInteractorImpl.KEY_COEXEC, mCoexec);
        outState.putString(LawDetailsInteractorImpl.KEY_DEPUTIES, mDdeputies);
        outState.putString(LawDetailsInteractorImpl.KEY_DEPARTMENTS, mDepartments);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawdetails);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.lawdetails_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

        setTitle(R.string.menu_laws);

        GosDumaApp.getComponent().inject(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mLaw = intent.getParcelableExtra(KEY_LAW);
        } else {
            mLaw = savedInstanceState.getParcelable(KEY_LAW);
        }

        mLawType.setText(mLaw.type);
        mLawName.setText(mLaw.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);
        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);
        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                final StringBuilder message = new StringBuilder();

                message.append(mLaw.type);
                message.append("\n");
                message.append(mLaw.getLawNameWithNumberAndDate());
                message.append("\n");
                if (mLaw.comments != null && !mLaw.comments.isEmpty()) {
                    message.append(getResources().getString(R.string.text_comment));
                    message.append(mLaw.comments);
                    message.append("\n");
                }
                if (mLaw.lastEventSolution != null && !mLaw.lastEventSolution.isEmpty()){
                    message.append(getResources().getString(R.string.text_solution));
                    message.append(mLaw.lastEventSolution);
                    message.append("\n");
                }
                if (mLaw.responsibleName != null && !mLaw.responsibleName.isEmpty()){
                    message.append(getResources().getString(R.string.text_resp_comittee));
                    message.append(mLaw.responsibleName);
                    message.append("\n");
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
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart(this);
            mPresenter.load(mLaw);
        }
    }

    @Override
    public void showMessage(int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showAdditionalData(){
        setTextToTextViewOrLayoutGone(mStage, mLawStage, mLayoutStage);
        setTextToTextViewOrLayoutGone(mPhase, mLawPhase, mLayoutPhase);

        setTextToTextViewOrLayoutGone(mProfile, mLawProgileComittees, mLayoutProfileComittees);
        setTextToTextViewOrLayoutGone(mCoexec, mLawCoexecComittees, mLayoutCoexecComittees);
        setTextToTextViewOrLayoutGone(mDdeputies, mLawDeputies, mLayoutDeputies);
        setTextToTextViewOrLayoutGone(mDepartments, mLawDepartments, mLayoutDepartments);
    }

    @Override
    public void showData(final HashMap<String, String> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStage = result.get(LawDetailsInteractorImpl.KEY_STAGE);
                mPhase = result.get(LawDetailsInteractorImpl.KEY_PHASE);
                mProfile = result.get(LawDetailsInteractorImpl.KEY_PROFILE);
                mCoexec = result.get(LawDetailsInteractorImpl.KEY_COEXEC);
                mDdeputies = result.get(LawDetailsInteractorImpl.KEY_DEPUTIES);
                mDepartments = result.get(LawDetailsInteractorImpl.KEY_DEPARTMENTS);

                showAdditionalData();
            }
        });
    }

    @Override
    public void showEmptyData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStage = "";
                mPhase = "";
                mProfile = "";
                mCoexec = "";
                mDdeputies = "";
                mDepartments = "";

                showAdditionalData();
            }
        });
    }

}
