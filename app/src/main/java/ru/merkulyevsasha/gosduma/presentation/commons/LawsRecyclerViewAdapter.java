package ru.merkulyevsasha.gosduma.presentation.commons;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;

/**
 * Created by sasha_merkulev on 01.10.2017.
 */

public class LawsRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Law> items;
    private final LawsRecyclerViewAdapter.OnLawClickListener onLawClickListener;
    private final Context context;
    private Deputy deputy;

    public LawsRecyclerViewAdapter(Context context, List<Law> items, LawsRecyclerViewAdapter.OnLawClickListener onLawClickListener) {
        this.context = context;
        this.items = items;
        this.onLawClickListener = onLawClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1 && deputy != null) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deputy_item2, parent, false);
            return new DeputyViewHolder(view);
        }
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_deputy_law_item, parent, false);
        return new LawsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LawsViewHolder) bindLawsViewHolder((LawsViewHolder) holder, position);
        else if (holder instanceof DeputyViewHolder)
            bindDeputyViewHolder((DeputyViewHolder) holder, position);
    }

    private void bindDeputyViewHolder(DeputyViewHolder holder, int position) {

        holder.deputyPosition.setText(deputy.getPositionWithStartAndEndDates());

        holder.deputyName.setText(deputy.getNameWithBirthday());
        String ranksAndDegrees = deputy.getRanksWithDegrees();
        if (ranksAndDegrees == null || ranksAndDegrees.isEmpty()){
            holder.deputyRanks.setVisibility(View.GONE);
        } else {
            holder.deputyRanks.setText(ranksAndDegrees);
            holder.deputyRanks.setVisibility(View.VISIBLE);
        }
        holder.fractionName.setText(deputy.fractionName);
        holder.fractionRole.setText(String.format("%s %s", deputy.fractionRole, deputy.fractionRegion));
        try {
            //noinspection ConstantConditions
            Picasso.with(context)
                    .load(context.getResources().getIdentifier("b"+String.valueOf(deputy.id), "raw", context.getPackageName()))
                    .into(holder.photo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void bindLawsViewHolder(LawsViewHolder holder, int position) {
        final Law law = items.get(position);
        String responsible = law.responsibleName;

        holder.lawName.setText(law.getLawNameWithNumberAndDate());
        if (responsible == null || responsible.isEmpty()) {
            holder.responsible.setVisibility(View.GONE);
        } else {
            holder.responsible.setVisibility(View.VISIBLE);
            holder.responsible.setText(responsible);
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

    public void setItems(List<Law> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void setDeputyItem(Deputy item) {
        this.deputy = item;
    }

    class LawsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_law_name) TextView lawName;
        @BindView(R.id.tv_law_responsible) TextView responsible;

        LawsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class DeputyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_deputy_name) TextView deputyName;
        @BindView(R.id.textview_position) TextView deputyPosition;
        @BindView(R.id.textview_deputy_fractionName) TextView fractionName;
        @BindView(R.id.textview_deputy_fractionRole) TextView fractionRole;
        @BindView(R.id.textview_deputy_ranks) TextView deputyRanks;
        @BindView(R.id.imageview_photo) ImageView photo;

        DeputyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface OnLawClickListener {
        void onLawClick(Law law);
    }

}
