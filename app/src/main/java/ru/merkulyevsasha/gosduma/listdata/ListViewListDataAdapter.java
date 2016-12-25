package ru.merkulyevsasha.gosduma.listdata;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.ListData;


public class ListViewListDataAdapter extends ArrayAdapter<ListData> {

    private final Context mContext;
    private final List<ListData> mItems;

    public ListViewListDataAdapter(Context context, List<ListData> items) {
        super(context, R.layout.listview_newsitem, items);

        mItems = items;
        mContext = context;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void addAll(@NonNull Collection<? extends ListData> collection) {
        super.addAll(collection);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.listview_newsitem, parent, false);
        TextView textViewTopic = (TextView) rowView.findViewById(R.id.textview_topic);

        ListData item = mItems.get(position);

        textViewTopic.setText(item.name);

        return rowView;
    }

}
