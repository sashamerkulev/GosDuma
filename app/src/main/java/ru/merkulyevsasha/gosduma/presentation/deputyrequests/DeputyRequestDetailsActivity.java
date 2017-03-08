package ru.merkulyevsasha.gosduma.presentation.deputyrequests;


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


@SuppressWarnings("WeakerAccess")
public class DeputyRequestDetailsActivity extends BaseActivity {


    public final static String KEY_DEPUTYREQUEST = "DEPUTYREQUEST";

    @BindView(R.id.tv_deputyrequest_name)
    public
    TextView mDeputyrequestName;

    @BindView(R.id.tv_deputyrequest_initiator)
    public
    TextView mDeputyrequestInitiator;

    @BindView(R.id.tv_deputyrequest_answer)
    public
    TextView mDeputyrequestAnswer;

    @BindView(R.id.tv_deputyrequest_resolution)
    public
    TextView mDeputyrequestResolution;

    @BindView(R.id.tv_deputyrequest_signed)
    public
    TextView mDeputyrequestSignedBy;

    @BindView(R.id.tv_deputyrequest_addressee)
    public
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

        setContentView(R.layout.activity_deputyrequest_details);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.deputyrequestdetails_toolbar);

        Intent intent = getIntent();
        mDeputyRequest = intent.getParcelableExtra(KEY_DEPUTYREQUEST);
        setTitle(R.string.menu_deputies_requests);

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

                message.append(mDeputyRequest.getNameWithNumberAndDate());
                message.append("\n");
                if (mDeputyRequest.initiator!= null && !mDeputyRequest.initiator.isEmpty()){
                    message.append(getString(R.string.text_initiator));
                    message.append(mDeputyRequest.initiator);
                    message.append("\n");
                }
                if (mDeputyRequest.answer!= null && !mDeputyRequest.answer.isEmpty()){
                    message.append(getString(R.string.text_answer));
                    message.append(mDeputyRequest.answer);
                    message.append("\n");
                }
                if (mDeputyRequest.resolution!= null && !mDeputyRequest.resolution.isEmpty()){
                    message.append(getString(R.string.text_resolution));
                    message.append(mDeputyRequest.resolution);
                    message.append("\n");
                }

                sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
            }
        });

    }


}
