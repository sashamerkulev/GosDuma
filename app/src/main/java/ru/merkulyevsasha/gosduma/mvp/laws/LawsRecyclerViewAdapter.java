package ru.merkulyevsasha.gosduma.mvp.laws;


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
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.mvp.ViewInterface;

public class LawsRecyclerViewAdapter  extends RecyclerView.Adapter<LawsRecyclerViewAdapter.LawsViewHolder>{

    public List<Law> mItems;

    private final ViewInterface.OnLawClickListener mClickListener;

    public LawsRecyclerViewAdapter(List<Law> items, ViewInterface.OnLawClickListener clickListener){
        mItems = items;
        mClickListener = clickListener;
    }

    @Override
    public LawsRecyclerViewAdapter.LawsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_deputy_law_item, parent, false);

        return new LawsRecyclerViewAdapter.LawsViewHolder(view, new ViewInterface.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                mClickListener.onLawClick(mItems.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(LawsRecyclerViewAdapter.LawsViewHolder holder, int position) {
        String name = mItems.get(position).name;
        String number = mItems.get(position).number;
        long date = mItems.get(position).introductionDate;
        String responsible = mItems.get(position).responsibleName;

        StringBuilder sb = new StringBuilder();

        if (date > 0) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            sb.append(number);
            sb.append(" ("+format.format(new Date(date))+")");
        } else {
            sb.append(number);
        }
        sb.append(" " + name);

        holder.mLawName.setText(sb.toString());
        if (responsible == null || responsible.isEmpty()){
            holder.mResponsible.setVisibility(View.GONE);
        } else {
            holder.mResponsible.setVisibility(View.VISIBLE);
            holder.mResponsible.setText(responsible);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class LawsViewHolder extends RecyclerView.ViewHolder{

        TextView mLawName;
        TextView mResponsible;

        public LawsViewHolder(final View itemView, final ViewInterface.OnClickListener clickListener) {
            super(itemView);
            mLawName = (TextView)itemView.findViewById(R.id.tv_law_name);
            mResponsible = (TextView)itemView.findViewById(R.id.tv_law_responsible);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }

    }

}
