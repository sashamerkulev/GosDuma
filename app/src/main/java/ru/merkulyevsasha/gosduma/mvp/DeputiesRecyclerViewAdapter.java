package ru.merkulyevsasha.gosduma.mvp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesRecyclerViewAdapter extends RecyclerView.Adapter<DeputiesRecyclerViewAdapter.DeputiesViewHolder> {

    public List<Deputy> mItems;

    private final ViewInterface.onDeputyClickListener mDeputyClickListener;

    public DeputiesRecyclerViewAdapter(List<Deputy> items, ViewInterface.onDeputyClickListener clickListener){
        mItems = items;
        mDeputyClickListener = clickListener;
    }

    @Override
    public DeputiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deputy_item, parent, false);

        return new DeputiesViewHolder(view, new ViewInterface.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                mDeputyClickListener.onDeputyClick(mItems.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(DeputiesViewHolder holder, int position) {
        String name = mItems.get(position).name;

        String positionName = mItems.get(position).position;
        boolean isCurrent = mItems.get(position).isCurrent;

        String fractionName = mItems.get(position).fractionName;
        String fractionRole = mItems.get(position).fractionRole;

        long birthdate = mItems.get(position).birthdate;
        if (birthdate != 0) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            holder.mDeputyName.setText(name + " ("+format.format(new Date(birthdate))+")");
        } else {
            holder.mDeputyName.setText(name);
        }

        if (isCurrent) {
            holder.mDeputyPosition.setText("Действующий " + positionName);
        } else {
            holder.mDeputyPosition.setText("Не действующий " + positionName);
        }

        if (!fractionRole.isEmpty()){
            holder.mDeputyFractionName.setText(fractionRole + " ("+fractionName+")");
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class DeputiesViewHolder extends RecyclerView.ViewHolder{

        TextView mDeputyName;
        TextView mDeputyPosition;
        TextView mDeputyFractionName;

        public DeputiesViewHolder(final View itemView, final ViewInterface.OnClickListener clickListener) {
            super(itemView);
            mDeputyName = (TextView)itemView.findViewById(R.id.deputy_name);
            mDeputyFractionName = (TextView)itemView.findViewById(R.id.deputy_fraction_name);
            mDeputyPosition = (TextView)itemView.findViewById(R.id.deputy_position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }

    }


}
