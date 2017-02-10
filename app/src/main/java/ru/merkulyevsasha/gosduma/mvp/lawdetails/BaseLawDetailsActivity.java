package ru.merkulyevsasha.gosduma.mvp.lawdetails;


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

import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrLayoutGone;

@SuppressWarnings("WeakerAccess")
@SuppressLint("Registered")
public class BaseLawDetailsActivity extends BaseActivity  {

    public final static String KEY_LAW = "LAW";

    public final static String KEY_STAGE = "STAGE";
    public final static String KEY_PHASE = "PHASE";
    public final static String KEY_PROFILE = "PROFILE";
    public final static String KEY_COEXEC = "COEXEC";
    public final static String KEY_DEPUTIES = "DEPUTIES";
    public final static String KEY_DEPARTMENTS = "DEPARTMENTS";


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

    private Law mLaw;
    private LawDetailsPresenter mPresenter;

    private String mStage;
    private String mPhase;
    private String mProfile;
    private String mCoexec;
    private String mDdeputies;
    private String mDepartments;

    private ReadLawDetailsAsyncTask mTask;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAW, mLaw);
        outState.putString(KEY_STAGE, mStage);
        outState.putString(KEY_PHASE, mPhase);
        outState.putString(KEY_PROFILE, mProfile);
        outState.putString(KEY_COEXEC, mCoexec);
        outState.putString(KEY_DEPUTIES, mDdeputies);
        outState.putString(KEY_DEPARTMENTS, mDepartments);
    }

    void initActivity(Bundle savedInstanceState){
        setContentView(R.layout.activity_lawdetails);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.lawdetails_toolbar);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mLaw = intent.getParcelableExtra(KEY_LAW);
        } else {
            mLaw = savedInstanceState.getParcelable(KEY_LAW);
        }

        setTitle("");

        mLawType.setText(mLaw.type);
        mLawName.setText(mLaw.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);
        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);
        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        if (savedInstanceState == null) {
            mPresenter = new LawDetailsPresenter(this);
            mTask = new ReadLawDetailsAsyncTask();
            mTask.execute();
        } else {
            mStage = savedInstanceState.getString(KEY_STAGE);
            mPhase = savedInstanceState.getString(KEY_PHASE);
            mProfile = savedInstanceState.getString(KEY_PROFILE);
            mCoexec = savedInstanceState.getString(KEY_COEXEC);
            mDdeputies = savedInstanceState.getString(KEY_DEPUTIES);
            mDepartments = savedInstanceState.getString(KEY_DEPARTMENTS);
            showAdditionalData();
        }

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
                    message.append(BaseLawDetailsActivity.this.getResources().getString(R.string.text_comment));
                    message.append(mLaw.comments);
                    message.append("\n");
                }
                if (mLaw.lastEventSolution != null && !mLaw.lastEventSolution.isEmpty()){
                    message.append(BaseLawDetailsActivity.this.getResources().getString(R.string.text_solution));
                    message.append(mLaw.lastEventSolution);
                    message.append("\n");
                }
                if (mLaw.responsibleName != null && !mLaw.responsibleName.isEmpty()){
                    message.append(BaseLawDetailsActivity.this.getResources().getString(R.string.text_resp_comittee));
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
    protected void onStop() {
        super.onStop();
        mTask.cancel(true);
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

    private class ReadLawDetailsAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {

            if (isCancelled())
                return;

            mStage = result.get(KEY_STAGE);
            mPhase = result.get(KEY_PHASE);
            mProfile = result.get(KEY_PROFILE);
            mCoexec = result.get(KEY_COEXEC);
            mDdeputies = result.get(KEY_DEPUTIES);
            mDepartments = result.get(KEY_DEPARTMENTS);

            showAdditionalData();
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {

            HashMap<String, String> result = new HashMap<>();

            if (isCancelled())
                return result;
            Codifier stage = mPresenter.getStageById(mLaw.lastEventStageId);
            result.put(KEY_STAGE, stage.name);
            if (isCancelled())
                return result;
            Codifier phase = mPresenter.getPhaseById(mLaw.lastEventPhaseId);
            result.put(KEY_PHASE, phase.name);

            if (isCancelled())
                return result;
            List<Codifier> profiles = mPresenter.getProfileComittees(mLaw.id);
            String sprofiles = joinCodifiers(profiles);
            result.put(KEY_PROFILE, sprofiles);

            if (isCancelled())
                return result;
            List<Codifier> coexecutors = mPresenter.getCoexecutorCommittees(mLaw.id);
            String scoexecutors = joinCodifiers(coexecutors);
            result.put(KEY_COEXEC, scoexecutors);

            if (isCancelled())
                return result;
            List<Codifier> deputies = mPresenter.getLawDeputies(mLaw.id);
            String sdeputies = joinCodifiers(deputies);
            result.put(KEY_DEPUTIES, sdeputies);

            if (isCancelled())
                return result;
            List<Codifier> federals = mPresenter.getLawFederals(mLaw.id);
            List<Codifier> regionals = mPresenter.getLawRegionals(mLaw.id);
            federals.addAll(regionals);
            String departments = joinCodifiers(federals);
            result.put(KEY_DEPARTMENTS, departments);

            return result;
        }
    }
}
