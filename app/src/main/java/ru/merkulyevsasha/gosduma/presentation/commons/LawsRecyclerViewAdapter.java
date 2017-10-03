package ru.merkulyevsasha.gosduma.presentation.commons;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Law;

/**
 * Created by sasha_merkulev on 01.10.2017.
 */

public class LawsRecyclerViewAdapter  extends RecyclerView.Adapter<LawsRecyclerViewAdapter.LawsViewHolder>{

    private final List<Law> items;
    private final LawsRecyclerViewAdapter.OnLawClickListener onLawClickListener;

    public LawsRecyclerViewAdapter(List<Law> items, LawsRecyclerViewAdapter.OnLawClickListener onLawClickListener){
        this.items = items;
        this.onLawClickListener = onLawClickListener;
    }

    @Override
    public LawsRecyclerViewAdapter.LawsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deputy_law_item, parent, false);

        return new LawsRecyclerViewAdapter.LawsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LawsRecyclerViewAdapter.LawsViewHolder holder, int position) {

        final Law law = items.get(position);
        String responsible = law.responsibleName;

        holder.mLawName.setText(law.getLawNameWithNumberAndDate());
        if (responsible == null || responsible.isEmpty()){
            holder.mResponsible.setVisibility(View.GONE);
        } else {
            holder.mResponsible.setVisibility(View.VISIBLE);
            holder.mResponsible.setText(responsible);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLawClickListener.onLawClick(law);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Law> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    class LawsViewHolder extends RecyclerView.ViewHolder{

        final TextView mLawName;
        final TextView mResponsible;

        LawsViewHolder(final View itemView) {
            super(itemView);
            mLawName = (TextView)itemView.findViewById(R.id.tv_law_name);
            mResponsible = (TextView)itemView.findViewById(R.id.tv_law_responsible);
        }

    }

    public interface OnLawClickListener{
        void onLawClick(Law law);
    }

}
