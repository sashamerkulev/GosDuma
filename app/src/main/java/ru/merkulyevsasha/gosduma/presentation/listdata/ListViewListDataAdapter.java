package ru.merkulyevsasha.gosduma.presentation.listdata;

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


class ListViewListDataAdapter extends ArrayAdapter<ListData> {

    private final List<ListData> mItems;
    private final LayoutInflater mInflater;

    public ListViewListDataAdapter(Context context, List<ListData> items) {
        super(context, R.layout.listview_newsitem, items);

        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listview_newsitem, parent, false);
            convertView.setTag(convertView.findViewById(R.id.textview_topic));
        }
        TextView textViewTopic = (TextView) convertView.getTag();

        ListData item = mItems.get(position);

        textViewTopic.setText(item.name);

        return convertView;
    }

}
