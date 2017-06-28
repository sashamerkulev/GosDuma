package ru.merkulyevsasha.gosduma.presentation.deputies;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.presentation.ViewInterface;


public class DeputiesRecyclerViewAdapter extends RecyclerView.Adapter<DeputiesRecyclerViewAdapter.DeputiesViewHolder> {

    public List<Deputy> mItems;

    private final DeputiesView.OnDeputyClickListener mClickListener;
    private final Context context;

    public DeputiesRecyclerViewAdapter(Context context, List<Deputy> items, DeputiesView.OnDeputyClickListener clickListener){
        mItems = items;
        mClickListener = clickListener;
        this.context = context;
    }

    @Override
    public DeputiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_deputy_item, parent, false);

        return new DeputiesViewHolder(view, new ViewInterface.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                mClickListener.onDeputyClick(mItems.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(DeputiesViewHolder holder, int position) {

        Deputy deputy = mItems.get(position);

        String fractionName = deputy.fractionName;
        String fractionRole = deputy.fractionRole;
        if (fractionRole != null)
            fractionRole = fractionRole.trim();

        holder.mDeputyName.setText(deputy.getNameWithBirthday());

        holder.mDeputyPosition.setText(deputy.getCurrentPosition());

        if (!fractionRole.isEmpty()) {
            holder.mDeputyFractionName.setText(fractionRole + " (" + fractionName + ")");
        }

        try {
            Picasso.with(context).load(context.getResources().getIdentifier("b"+String.valueOf(deputy.id), "raw", context.getPackageName()))
                    .into(holder.mPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class DeputiesViewHolder extends RecyclerView.ViewHolder{

        final TextView mDeputyName;
        final TextView mDeputyPosition;
        final TextView mDeputyFractionName;
        final ImageView mPhoto;

        public DeputiesViewHolder(final View itemView, final ViewInterface.OnClickListener clickListener) {
            super(itemView);
            mDeputyName = (TextView)itemView.findViewById(R.id.deputy_name);
            mDeputyFractionName = (TextView)itemView.findViewById(R.id.deputy_fraction_name);
            mDeputyPosition = (TextView)itemView.findViewById(R.id.deputy_position);
            mPhoto = (ImageView)itemView.findViewById(R.id.imageview_photo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }

    }


}
