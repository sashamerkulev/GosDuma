package ru.merkulyevsasha.gosduma.mvp.deputyrequests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

import static ru.merkulyevsasha.gosduma.mvp.deputyrequests.DeputyRequestDetailsActivity.KEY_DEPUTYREQUEST;
import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrLayoutGone;


public class DeputyRequestDetailsFragment extends Fragment {

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

    public static DeputyRequestDetailsFragment newInstance(DeputyRequest deputyRequest) {
        DeputyRequestDetailsFragment fragment = new DeputyRequestDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_DEPUTYREQUEST, deputyRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deputyrequest_details, container, false);

        final DeputyRequest mDeputyRequest = getArguments().getParcelable(KEY_DEPUTYREQUEST);

        assert mDeputyRequest != null;

        TextView mDeputyrequestName = (TextView)v.findViewById(R.id.tv_deputyrequest_name);
        TextView mDeputyrequestInitiator = (TextView)v.findViewById(R.id.tv_deputyrequest_initiator);
        TextView mDeputyrequestAnswer = (TextView)v.findViewById(R.id.tv_deputyrequest_answer);
        TextView mDeputyrequestResolution = (TextView)v.findViewById(R.id.tv_deputyrequest_resolution);
        TextView mDeputyrequestSignedBy = (TextView)v.findViewById(R.id.tv_deputyrequest_signed);
        TextView mDeputyrequestAddressee = (TextView)v.findViewById(R.id.tv_deputyrequest_addressee);

        LinearLayout mInitiatorLayout = (LinearLayout)v.findViewById(R.id.layout_initiator);
        LinearLayout mAnswerLayout = (LinearLayout)v.findViewById(R.id.layout_answer);
        LinearLayout mResolutionLayout = (LinearLayout)v.findViewById(R.id.layout_resolution);
        LinearLayout mSignedLayout = (LinearLayout)v.findViewById(R.id.layout_signed);
        LinearLayout mAddresseeLayout = (LinearLayout)v.findViewById(R.id.layout_addressee);

        mDeputyrequestName.setText(mDeputyRequest.getNameWithNumberAndDate());

        setTextToTextViewOrLayoutGone(mDeputyRequest.initiator, mDeputyrequestInitiator, mInitiatorLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.answer, mDeputyrequestAnswer, mAnswerLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.resolution, mDeputyrequestResolution, mResolutionLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.signedBy_name, mDeputyrequestSignedBy, mSignedLayout);
        setTextToTextViewOrLayoutGone(mDeputyRequest.addressee_name, mDeputyrequestAddressee, mAddresseeLayout);


        return v;
    }

}
