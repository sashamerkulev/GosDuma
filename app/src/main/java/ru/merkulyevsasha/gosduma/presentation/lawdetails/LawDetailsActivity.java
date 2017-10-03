package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import ru.merkulyevsasha.gosduma.models.Law;

import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractorImpl;
import ru.merkulyevsasha.gosduma.helpers.UiUtils;

import static ru.merkulyevsasha.gosduma.helpers.UiUtils.setTextToTextViewOrLayoutGone;


public class LawDetailsActivity extends AppCompatActivity implements LawDetailsView{

    public final static String KEY_LAW = "LAW";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar_layout) AppBarLayout appbarLayout;

    @BindView(R.id.tv_law_type) TextView mLawType;
    @BindView(R.id.tv_law_name) TextView mLawName;
    @BindView(R.id.tv_law_comments) TextView mLawComments;
    @BindView(R.id.tv_law_responsible) TextView mLawResponsible;
    @BindView(R.id.tv_law_solution) TextView mLawSolution;
    @BindView(R.id.tv_law_stage) TextView mLawStage;
    @BindView(R.id.tv_law_phase) TextView mLawPhase;
    @BindView(R.id.tv_law_coexeccomittees) TextView mLawCoexecComittees;
    @BindView(R.id.tv_law_profilecomittees)TextView mLawProgileComittees;
    @BindView(R.id.tv_law_deputies) TextView mLawDeputies;
    @BindView(R.id.tv_law_departments) TextView mLawDepartments;
    @BindView(R.id.layout_comments) LinearLayout mLayoutComments;
    @BindView(R.id.layout_phase) LinearLayout mLayoutPhase;
    @BindView(R.id.layout_stage) LinearLayout mLayoutStage;
    @BindView(R.id.layout_responsible) LinearLayout mLayoutResponsible;
    @BindView(R.id.layout_solution) LinearLayout mLayoutSolution;
    @BindView(R.id.layout_coexeccomittees) LinearLayout mLayoutCoexecComittees;
    @BindView(R.id.layout_profilecomittees) LinearLayout mLayoutProfileComittees;
    @BindView(R.id.layout_deputies) LinearLayout mLayoutDeputies;
    @BindView(R.id.layout_departments) LinearLayout mLayoutDepartments;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        setTitle(R.string.menu_laws);

        GosDumaApp.getComponent().inject(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mLaw = intent.getParcelableExtra(KEY_LAW);
        } else {
            mLaw = savedInstanceState.getParcelable(KEY_LAW);
        }
        if (mLaw == null) finish();

        mLawType.setText(mLaw.type);
        mLawName.setText(mLaw.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);
        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);
        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSharedClicked(mLaw);
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
    public void onPause() {
        super.onPause();
        mPresenter.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onStart(this);
        mPresenter.load(mLaw);
    }

    @Override
    public void showMessage(int resId) {
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
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
        mStage = result.get(LawDetailsInteractorImpl.KEY_STAGE);
        mPhase = result.get(LawDetailsInteractorImpl.KEY_PHASE);
        mProfile = result.get(LawDetailsInteractorImpl.KEY_PROFILE);
        mCoexec = result.get(LawDetailsInteractorImpl.KEY_COEXEC);
        mDdeputies = result.get(LawDetailsInteractorImpl.KEY_DEPUTIES);
        mDepartments = result.get(LawDetailsInteractorImpl.KEY_DEPARTMENTS);

        showAdditionalData();
    }

    @Override
    public void showEmptyData() {
        mStage = "";
        mPhase = "";
        mProfile = "";
        mCoexec = "";
        mDdeputies = "";
        mDepartments = "";

        showAdditionalData();
    }

    @Override
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

    public static void startScreen(Context context, Law law) {
        Intent activityIntent = new Intent(context, LawDetailsActivity.class);
        activityIntent.putExtra(LawDetailsActivity.KEY_LAW, law);
        context.startActivity(activityIntent);
    }
}
