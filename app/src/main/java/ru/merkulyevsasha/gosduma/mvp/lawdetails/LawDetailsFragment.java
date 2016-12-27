package ru.merkulyevsasha.gosduma.mvp.lawdetails;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Law;

import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_PROFILE;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_COEXEC;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_DEPARTMENTS;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_DEPUTIES;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_LAW;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_PHASE;
import static ru.merkulyevsasha.gosduma.mvp.lawdetails.BaseLawDetailsActivity.KEY_STAGE;
import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrLayoutGone;


public class LawDetailsFragment extends Fragment{

    private TextView mLawType;
    private TextView mLawName;
    private TextView mLawComments;
    private TextView mLawResponsible;
    private TextView mLawSolution;
    private TextView mLawStage;
    private TextView mLawPhase;
    private TextView mLawCoexecComittees;
    private TextView mLawProgileComittees;
    private TextView mLawDeputies;
    private TextView mLawDepartments;

    private LinearLayout mLayoutComments;
    private LinearLayout mLayoutPhase;
    private LinearLayout mLayoutStage;
    private LinearLayout mLayoutResponsible;
    private LinearLayout mLayoutSolution;
    private LinearLayout mLayoutCoexecComittees;
    private LinearLayout mLayoutProfileComittees;
    private LinearLayout mLayoutDeputies;
    private LinearLayout mLayoutDepartments;

    private String mStage;
    private String mPhase;
    private String mProfile;
    private String mCoexec;
    private String mDdeputies;
    private String mDepartments;

    Law mLaw;
    private LawDetailsPresenter mPresenter;

    public static LawDetailsFragment newInstance(Law law) {
        LawDetailsFragment fragment = new LawDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_LAW, law);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_law_details, container, false);

        mLaw = getArguments().getParcelable(KEY_LAW);

        assert mLaw != null;

        mLawType = (TextView)v.findViewById(R.id.tv_law_type);
        mLawName = (TextView)v.findViewById(R.id.tv_law_name);
        mLawComments = (TextView)v.findViewById(R.id.tv_law_comments);
        mLawResponsible = (TextView)v.findViewById(R.id.tv_law_responsible);
        mLawSolution = (TextView)v.findViewById(R.id.tv_law_solution);
        mLawStage = (TextView)v.findViewById(R.id.tv_law_stage);
        mLawPhase = (TextView)v.findViewById(R.id.tv_law_phase);
        mLawCoexecComittees = (TextView)v.findViewById(R.id.tv_law_coexeccomittees);
        mLawProgileComittees = (TextView)v.findViewById(R.id.tv_law_profilecomittees);
        mLawDeputies = (TextView)v.findViewById(R.id.tv_law_deputies);
        mLawDepartments = (TextView)v.findViewById(R.id.tv_law_departments);

        mLayoutComments = (LinearLayout)v.findViewById(R.id.layout_comments);
        mLayoutPhase = (LinearLayout)v.findViewById(R.id.layout_phase);
        mLayoutStage = (LinearLayout)v.findViewById(R.id.layout_stage);
        mLayoutResponsible = (LinearLayout)v.findViewById(R.id.layout_responsible);
        mLayoutSolution = (LinearLayout)v.findViewById(R.id.layout_solution);
        mLayoutCoexecComittees = (LinearLayout)v.findViewById(R.id.layout_coexeccomittees);
        mLayoutProfileComittees = (LinearLayout)v.findViewById(R.id.layout_profilecomittees);
        mLayoutDeputies = (LinearLayout)v.findViewById(R.id.layout_deputies);
        mLayoutDepartments = (LinearLayout)v.findViewById(R.id.layout_departments);


        mLawType.setText(mLaw.type);
        mLawName.setText(mLaw.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);
        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);
        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        mPresenter = new LawDetailsPresenter(getActivity());
        LawDetailsFragment.MyAsyncTask task = new LawDetailsFragment.MyAsyncTask();
        task.execute();

        return v;
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

    private class MyAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected void onPreExecute() {

            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(HashMap<String, String> result) {

            mStage = result.get(KEY_STAGE);
            mPhase = result.get(KEY_PHASE);
            mProfile = result.get(KEY_PROFILE);
            mCoexec = result.get(KEY_COEXEC);
            mDdeputies = result.get(KEY_DEPUTIES);
            mDepartments = result.get(KEY_DEPARTMENTS);

            showAdditionalData();
            //mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {

            HashMap<String, String> result = new HashMap<>();

            Codifier stage = mPresenter.getStageById(mLaw.lastEventStageId);
            result.put(KEY_STAGE, stage.name);
            Codifier phase = mPresenter.getPhaseById(mLaw.lastEventPhaseId);
            result.put(KEY_PHASE, phase.name);

            List<Codifier> profiles = mPresenter.getProfileComittees(mLaw.id);
            String sprofiles = joinCodifiers(profiles);
            result.put(KEY_PROFILE, sprofiles);

            List<Codifier> coexecutors = mPresenter.getCoexecutorCommittees(mLaw.id);
            String scoexecutors = joinCodifiers(coexecutors);
            result.put(KEY_COEXEC, scoexecutors);

            List<Codifier> deputies = mPresenter.getLawDeputies(mLaw.id);
            String sdeputies = joinCodifiers(deputies);
            result.put(KEY_DEPUTIES, sdeputies);

            List<Codifier> federals = mPresenter.getLawFederals(mLaw.id);
            List<Codifier> regionals = mPresenter.getLawRegionals(mLaw.id);
            federals.addAll(regionals);
            String departments = joinCodifiers(federals);
            result.put(KEY_DEPARTMENTS, departments);

            return result;
        }
    }

}
