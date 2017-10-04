package ru.merkulyevsasha.gosduma.presentation.deputyrequestdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;

import static ru.merkulyevsasha.gosduma.helpers.UiUtils.setTextToTextViewOrLayoutGone;


public class DeputyRequestDetailsFragment extends Fragment {

    @BindView(R.id.tv_deputyrequest_name) TextView deputyrequestName;
    @BindView(R.id.tv_deputyrequest_initiator) TextView deputyrequestInitiator;
    @BindView(R.id.tv_deputyrequest_answer) TextView deputyrequestAnswer;
    @BindView(R.id.tv_deputyrequest_resolution) TextView deputyrequestResolution;
    @BindView(R.id.tv_deputyrequest_signed) TextView deputyrequestSignedBy;
    @BindView(R.id.tv_deputyrequest_addressee) TextView deputyrequestAddressee;
    @BindView(R.id.layout_initiator) LinearLayout initiatorLayout;
    @BindView(R.id.layout_answer) LinearLayout answerLayout;
    @BindView(R.id.layout_resolution) LinearLayout resolutionLayout;
    @BindView(R.id.layout_signed) LinearLayout signedLayout;
    @BindView(R.id.layout_addressee) LinearLayout addresseeLayout;

    public static DeputyRequestDetailsFragment newInstance(DeputyRequest deputyRequest) {
        DeputyRequestDetailsFragment fragment = new DeputyRequestDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KeysBundleHolder.KEY_DEPUTYREQUEST, deputyRequest);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deputyrequest_details, container, false);
        ButterKnife.bind(this, v);
        if (getArguments() == null) return v;
        final DeputyRequest deputyRequest = getArguments().getParcelable(KeysBundleHolder.KEY_DEPUTYREQUEST);
        if (deputyRequest == null) return v;
        deputyrequestName.setText(deputyRequest.getNameWithNumberAndDate());

        setTextToTextViewOrLayoutGone(deputyRequest.initiator, deputyrequestInitiator, initiatorLayout);
        setTextToTextViewOrLayoutGone(deputyRequest.answer, deputyrequestAnswer, answerLayout);
        setTextToTextViewOrLayoutGone(deputyRequest.resolution, deputyrequestResolution, resolutionLayout);
        setTextToTextViewOrLayoutGone(deputyRequest.signedBy_name, deputyrequestSignedBy, signedLayout);
        setTextToTextViewOrLayoutGone(deputyRequest.addressee_name, deputyrequestAddressee, addresseeLayout);

        return v;
    }

}
