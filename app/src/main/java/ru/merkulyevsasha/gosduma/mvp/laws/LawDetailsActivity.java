package ru.merkulyevsasha.gosduma.mvp.laws;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.tv_law_docname)
    public TextView mLawDocname;

    @BindView(R.id.tv_law_solution)
    public TextView mLawSolution;

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

        initToolbar(R.id.lawdetails_toolbar);

        Intent intent = getIntent();
        mLaw = intent.getParcelableExtra("law");
        setTitle("");

        mLawType.setText(mLaw.type);

        mLawName.setText(mLaw.getLawNameWithNumberAndDate());

        setText(mLaw.comments, mLawComments);
        setText(mLaw.responsibleName, mLawResponsible);
        mLawDocname.setText(mLaw.lastEventDocType + " " + mLaw.lastEventDocName);
        setText(mLaw.lastEventSolution, mLawSolution);


    }

    @Override
    public void show(List<Deputy> items) {

    }
}
