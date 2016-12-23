package ru.merkulyevsasha.gosduma.mvp.laws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Codifier;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;


public class LawDetailsActivity extends BaseActivity
        implements ViewInterface {

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

    private Law mLaw;
    private LawsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLargeLandscape()) {
            finish();
            return;
        }

        mPresenter = new LawsPresenter(this, this);

        setContentView(R.layout.activity_lawdetails);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.lawdetails_toolbar);

        Intent intent = getIntent();
        mLaw = intent.getParcelableExtra("law");
        setTitle("");

        mLawType.setText(mLaw.type);

        mLawName.setText(mLaw.getLawNameWithNumberAndDate());

        setTextToTextViewOrLayoutGone(mLaw.comments, mLawComments, mLayoutComments);

        Codifier stage = mPresenter.getStageById(mLaw.lastEventStageId);
        setTextToTextViewOrLayoutGone(stage.name, mLawStage, mLayoutStage);

        Codifier phase = mPresenter.getPhaseById(mLaw.lastEventPhaseId);
        setTextToTextViewOrLayoutGone(phase.name, mLawPhase, mLayoutPhase);

        setTextToTextViewOrLayoutGone(mLaw.lastEventSolution, mLawSolution, mLayoutSolution);

        setTextToTextViewOrLayoutGone(mLaw.responsibleName, mLawResponsible, mLayoutResponsible);

        List<Codifier> profiles = mPresenter.getProfileComittees(mLaw.id);
        String sprofiles = joinCodifiers(profiles);
        setTextToTextViewOrLayoutGone(sprofiles, mLawProgileComittees, mLayoutProfileComittees);

        List<Codifier> coexecutors = mPresenter.getCoexecutorCommittees(mLaw.id);
        String scoexecutors = joinCodifiers(coexecutors);
        setTextToTextViewOrLayoutGone(scoexecutors, mLawCoexecComittees, mLayoutCoexecComittees);

        List<Codifier> deputies = mPresenter.getLawDeputies(mLaw.id);
        String sdeputies = joinCodifiers(deputies);
        setTextToTextViewOrLayoutGone(sdeputies, mLawDeputies, mLayoutDeputies);

        List<Codifier> federals = mPresenter.getLawFederals(mLaw.id);
        List<Codifier> regionals = mPresenter.getLawRegionals(mLaw.id);
        federals.addAll(regionals);
        String departments = joinCodifiers(federals);
        setTextToTextViewOrLayoutGone(departments, mLawDepartments, mLayoutDepartments);

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

    @Override
    public void show(List<Deputy> items) {

    }
}
