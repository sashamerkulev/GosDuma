package ru.merkulyevsasha.gosduma.mvp.deputyrequests;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;


public class DeputyRequestsRecyclerViewAdapter extends RecyclerView.Adapter<DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder> {


    public List<DeputyRequest> mItems;

    private final OnDeputyRequestsClickListener mClickListener;

    public DeputyRequestsRecyclerViewAdapter(List<DeputyRequest> items, OnDeputyRequestsClickListener clickListener){
        mItems = items;
        mClickListener = clickListener;
    }

    @Override
    public DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_deputy_request_item, parent, false);

        return new DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder(view, new ViewInterface.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                mClickListener.onDeputyRequestwClick(mItems.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(DeputyRequestsRecyclerViewAdapter.DeputyRequestsViewHolder holder, int position) {

        DeputyRequest deputyRequest = mItems.get(position);

        holder.mDeputyRequestName.setText(deputyRequest.getNameWithNumberAndDate());
        holder.mDeputyRequestInitiator.setText(deputyRequest.initiator);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class DeputyRequestsViewHolder extends RecyclerView.ViewHolder{

        final TextView mDeputyRequestName;
        final TextView mDeputyRequestInitiator;

        public DeputyRequestsViewHolder(final View itemView, final ViewInterface.OnClickListener clickListener) {
            super(itemView);
            mDeputyRequestName = (TextView)itemView.findViewById(R.id.deputyrequest_name);
            mDeputyRequestInitiator = (TextView)itemView.findViewById(R.id.deputyrequest_initiator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }

    }

}
