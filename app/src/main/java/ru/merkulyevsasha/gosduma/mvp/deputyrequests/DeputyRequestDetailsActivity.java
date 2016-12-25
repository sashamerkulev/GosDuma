package ru.merkulyevsasha.gosduma.mvp.deputyrequests;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrLayoutGone;


public class DeputyRequestDetailsActivity extends BaseActivity {


    public final static String KEY_DEPUTYREQUEST = "DEPUTYREQUEST";

    @BindView(R.id.tv_deputyrequest_name)
    TextView mDeputyrequestName;

    @BindView(R.id.tv_deputyrequest_initiator)
    TextView mDeputyrequestInitiator;

    @BindView(R.id.tv_deputyrequest_answer)
    TextView mDeputyrequestAnswer;

    @BindView(R.id.tv_deputyrequest_resolution)
    TextView mDeputyrequestResolution;

    @BindView(R.id.tv_deputyrequest_signed)
    TextView mDeputyrequestSignedBy;

    @BindView(R.id.tv_deputyrequest_addressee)
    TextView mDeputyrequestAddressee;


    @BindView(R.id.layout_initiator)
    public LinearLayout mInitiatorLayout;

    @BindView(R.id.layout_answer)
    public LinearLayout mAnswerLayout;

    @BindView(R.id.layout_resolution)
    public LinearLayout mResolutionLayout;

    @BindView(R.id.layout_signed)
    public LinearLayout mSignedLayout;

    @BindView(R.id.layout_addressee)
    public LinearLayout mAddresseeLayout;

    @BindView(R.id.fab)
    public FloatingActionButton mFab;

    private DeputyRequest mDeputyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isLargeLandscape()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_deputyrequestdetails);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.deputyrequestdetails_toolbar);

        Intent intent = getIntent();
        mDeputyRequest = intent.getParcelableExtra(KEY_DEPUTYREQUEST);
        setTitle("");

        mDeputyrequestName.setText(mDeputyRequest.getNameWithNumberAndDate());

        setTextToTextViewOrLayoutGone(mDeputyRequest.initiator, mDeputyrequestInitiator, mInitiatorLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.answer, mDeputyrequestAnswer, mAnswerLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.resolution, mDeputyrequestResolution, mResolutionLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.signedBy_name, mDeputyrequestSignedBy, mSignedLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.addressee_name, mDeputyrequestAddressee, mAddresseeLayout);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                final StringBuilder message = new StringBuilder();

                sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
            }
        });

    }


}
