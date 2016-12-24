package ru.merkulyevsasha.gosduma.mvp.laws;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.LawsViewInterface;

@SuppressLint("Registered")
public class BaseLawDetailsActivity extends BaseActivity
        implements LawsViewInterface {

    private final static String KEY_BUNDLE_LAW = "LAW";
    private final static String KEY_BUNDLE_STAGE = "STAGE";
    private final static String KEY_BUNDLE_PHASE = "PHASE";
    private final static String KEY_BUNDLE_PROFILE = "PROFILE";
    private final static String KEY_BUNDLE_COEXEC = "COEXEC";
    private final static String KEY_BUNDLE_DEPUTIES = "DEPUTIES";
    private final static String KEY_BUNDLE_DEPARTMENTS = "DEPARTMENTS";


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

    Law mLaw;
    private DeputyLawsPresenter mPresenter;

    private String mStage;
    private String mPhase;
    private String mProfile;
    private String mCoexec;
    private String mDdeputies;
    private String mDepartments;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_BUNDLE_LAW, mLaw);
        outState.putString(KEY_BUNDLE_STAGE, mStage);
        outState.putString(KEY_BUNDLE_PHASE, mPhase);
        outState.putString(KEY_BUNDLE_PROFILE, mProfile);
        outState.putString(KEY_BUNDLE_COEXEC, mCoexec);
        outState.putString(KEY_BUNDLE_DEPUTIES, mDdeputies);
        outState.putString(KEY_BUNDLE_DEPARTMENTS, mDepartments);
    }

    void initActivity(Bundle savedInstanceState){
        setContentView(R.layout.activity_lawdetails);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.lawdetails_toolbar);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mLaw = intent.getParcelableExtra("law");
        } else {
            mLaw = savedInstanceState.getParcelable(KEY_BUNDLE_LAW);
        }

        setTitle("");

        mLawType.setText(mLaw.type);
        mLawName.setText(mLaw.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);
        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);
        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        if (savedInstanceState == null) {
            mPresenter = new DeputyLawsPresenter(this, this);
            MyAsyncTask task = new MyAsyncTask();
            task.execute();
        } else {
            mStage = savedInstanceState.getString(KEY_BUNDLE_STAGE);
            mPhase = savedInstanceState.getString(KEY_BUNDLE_PHASE);
            mProfile = savedInstanceState.getString(KEY_BUNDLE_PROFILE);
            mCoexec = savedInstanceState.getString(KEY_BUNDLE_COEXEC);
            mDdeputies = savedInstanceState.getString(KEY_BUNDLE_DEPUTIES);
            mDepartments = savedInstanceState.getString(KEY_BUNDLE_DEPARTMENTS);
            showAdditionalData();
        }

    }

    private String joinCodifiers(List<Codifier> list){
        StringBuilder sb = new StringBuilder();

        for (Codifier item : list) {
            if (sb.length() > 0){
                sb.append(", ");
            }
            sb.append(item.name);
        }

        return sb.toString();
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
    public void show(List<Law> items) {

    }

    private class MyAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {

            mStage = result.get(KEY_BUNDLE_STAGE);
            mPhase = result.get(KEY_BUNDLE_PHASE);
            mProfile = result.get(KEY_BUNDLE_PROFILE);
            mCoexec = result.get(KEY_BUNDLE_COEXEC);
            mDdeputies = result.get(KEY_BUNDLE_DEPUTIES);
            mDepartments = result.get(KEY_BUNDLE_DEPARTMENTS);

            showAdditionalData();
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {

            HashMap<String, String> result = new HashMap<String, String>();

            Codifier stage = mPresenter.getStageById(mLaw.lastEventStageId);
            result.put(KEY_BUNDLE_STAGE, stage.name);
            Codifier phase = mPresenter.getPhaseById(mLaw.lastEventPhaseId);
            result.put(KEY_BUNDLE_PHASE, phase.name);

            List<Codifier> profiles = mPresenter.getProfileComittees(mLaw.id);
            String sprofiles = joinCodifiers(profiles);
            result.put(KEY_BUNDLE_PROFILE, sprofiles);

            List<Codifier> coexecutors = mPresenter.getCoexecutorCommittees(mLaw.id);
            String scoexecutors = joinCodifiers(coexecutors);
            result.put(KEY_BUNDLE_COEXEC, scoexecutors);

            List<Codifier> deputies = mPresenter.getLawDeputies(mLaw.id);
            String sdeputies = joinCodifiers(deputies);
            result.put(KEY_BUNDLE_DEPUTIES, sdeputies);

            List<Codifier> federals = mPresenter.getLawFederals(mLaw.id);
            List<Codifier> regionals = mPresenter.getLawRegionals(mLaw.id);
            federals.addAll(regionals);
            String departments = joinCodifiers(federals);
            result.put(KEY_BUNDLE_DEPARTMENTS, departments);

            return result;
        }
    }
}
