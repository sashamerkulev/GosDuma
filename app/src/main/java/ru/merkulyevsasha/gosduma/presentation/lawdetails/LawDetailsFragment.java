package ru.merkulyevsasha.gosduma.presentation.lawdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractorImpl;
import ru.merkulyevsasha.gosduma.models.Law;

import static ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity.KEY_LAW;
import static ru.merkulyevsasha.gosduma.helpers.UiUtils.setTextToTextViewOrLayoutGone;


public class LawDetailsFragment extends Fragment implements LawDetailsView{

    @Inject public LawDetailsPresenter pres;

    @BindView(R.id.tv_law_type) TextView lawType;
    @BindView(R.id.tv_law_name) TextView lawName;
    @BindView(R.id.tv_law_comments) TextView lawComments;
    @BindView(R.id.tv_law_responsible) TextView lawResponsible;
    @BindView(R.id.tv_law_solution) TextView lawSolution;
    @BindView(R.id.tv_law_stage) TextView lawStage;
    @BindView(R.id.tv_law_phase) TextView lawPhase;
    @BindView(R.id.tv_law_coexeccomittees) TextView lawCoexecComittees;
    @BindView(R.id.tv_law_profilecomittees)TextView lawProgileComittees;
    @BindView(R.id.tv_law_deputies) TextView lawDeputies;
    @BindView(R.id.tv_law_departments) TextView lawDepartments;
    @BindView(R.id.layout_comments) LinearLayout layoutComments;
    @BindView(R.id.layout_phase) LinearLayout layoutPhase;
    @BindView(R.id.layout_stage) LinearLayout layoutStage;
    @BindView(R.id.layout_responsible) LinearLayout layoutResponsible;
    @BindView(R.id.layout_solution) LinearLayout layoutSolution;
    @BindView(R.id.layout_coexeccomittees) LinearLayout layoutCoexecComittees;
    @BindView(R.id.layout_profilecomittees) LinearLayout layoutProfileComittees;
    @BindView(R.id.layout_deputies) LinearLayout layoutDeputies;
    @BindView(R.id.layout_departments) LinearLayout layoutDepartments;

    private String stage;
    private String phase;
    private String profile;
    private String coexec;
    private String deputies;
    private String departments;

    private Law law;

    private View rootView;

    private Unbinder unbinder;

    public static LawDetailsFragment newInstance(Law law) {
        LawDetailsFragment fragment = new LawDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_LAW, law);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_law_details, container, false);
        if (getArguments() == null) return rootView;
        law = getArguments().getParcelable(KEY_LAW);
        if (law == null) return rootView;
        unbinder = ButterKnife.bind(this, rootView );

        lawType.setText(law.type);
        lawName.setText(law.getLawNameWithNumberAndDate());
        setTextToTextViewOrLayoutGone(law.comments, lawComments, layoutComments);
        setTextToTextViewOrLayoutGone(law.lastEventSolution, lawSolution, layoutSolution);
        setTextToTextViewOrLayoutGone(law.responsibleName, lawResponsible, layoutResponsible);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pres != null) {
            pres.unbind();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (pres != null) {
            pres.bind(this);
            pres.load(law);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        pres.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        Snackbar.make(rootView, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showProgress() {
    }

    private void showAdditionalData(){
        setTextToTextViewOrLayoutGone(stage, lawStage, layoutStage);
        setTextToTextViewOrLayoutGone(phase, lawPhase, layoutPhase);

        setTextToTextViewOrLayoutGone(profile, lawProgileComittees, layoutProfileComittees);
        setTextToTextViewOrLayoutGone(coexec, lawCoexecComittees, layoutCoexecComittees);
        setTextToTextViewOrLayoutGone(deputies, lawDeputies, layoutDeputies);
        setTextToTextViewOrLayoutGone(departments, lawDepartments, layoutDepartments);
    }

    @Override
    public void showData(final HashMap<String, String> result) {
        stage = result.get(LawDetailsInteractorImpl.KEY_STAGE);
        phase = result.get(LawDetailsInteractorImpl.KEY_PHASE);
        profile = result.get(LawDetailsInteractorImpl.KEY_PROFILE);
        coexec = result.get(LawDetailsInteractorImpl.KEY_COEXEC);
        deputies = result.get(LawDetailsInteractorImpl.KEY_DEPUTIES);
        departments = result.get(LawDetailsInteractorImpl.KEY_DEPARTMENTS);

        showAdditionalData();
    }

    @Override
    public void showEmptyData() {
        stage = "";
        phase = "";
        profile = "";
        coexec = "";
        deputies = "";
        departments = "";

        showAdditionalData();
    }

//
//
//    private String joinCodifiers(List<Codifier> list){
//        StringBuilder sb = new StringBuilder();
//
//        for (Codifier item : list) {
//            if (sb.length() > 0){
//                sb.append(", ");
//            }
//            sb.append(item.name);
//        }
//
//        return sb.toString();
//    }
//
//
//    private class MyAsyncTask extends AsyncTask<Void, Void, HashMap<String, String>> {
//
//        @Override
//        protected void onPreExecute() {
//
//            //mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected void onPostExecute(HashMap<String, String> result) {
//
//            stage = result.get(KEY_STAGE);
//            phase = result.get(KEY_PHASE);
//            profile = result.get(KEY_PROFILE);
//            coexec = result.get(KEY_COEXEC);
//            deputies = result.get(KEY_DEPUTIES);
//            departments = result.get(KEY_DEPARTMENTS);
//
//            showAdditionalData();
//            //mProgressBar.setVisibility(View.GONE);
//        }
//
//        @Override
//        protected HashMap<String, String> doInBackground(Void... params) {
//
//            HashMap<String, String> result = new HashMap<>();
//
//            Codifier stage = pres.getStageById(law.lastEventStageId);
//            result.put(KEY_STAGE, stage.name);
//            Codifier phase = pres.getPhaseById(law.lastEventPhaseId);
//            result.put(KEY_PHASE, phase.name);
//
//            List<Codifier> profiles = pres.getProfileComittees(law.id);
//            String sprofiles = joinCodifiers(profiles);
//            result.put(KEY_PROFILE, sprofiles);
//
//            List<Codifier> coexecutors = pres.getCoexecutorCommittees(law.id);
//            String scoexecutors = joinCodifiers(coexecutors);
//            result.put(KEY_COEXEC, scoexecutors);
//
//            List<Codifier> deputies = pres.getLawDeputies(law.id);
//            String sdeputies = joinCodifiers(deputies);
//            result.put(KEY_DEPUTIES, sdeputies);
//
//            List<Codifier> federals = pres.getLawFederals(law.id);
//            List<Codifier> regionals = pres.getLawRegionals(law.id);
//            federals.addAll(regionals);
//            String departments = joinCodifiers(federals);
//            result.put(KEY_DEPARTMENTS, departments);
//
//            return result;
//        }
//    }

}
