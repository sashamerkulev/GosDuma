package ru.merkulyevsasha.gosduma.ui;


import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Article;

public class ListViewNewsAdapter extends ArrayAdapter<Article> {

    private final Context mContext;
    private final List<Article> mItems;

    public ListViewNewsAdapter(Context context, List<Article> items) {
        super(context, R.layout.listview_newsitem);

        mItems = items;
        mContext = context;
    }

    @Override
    public void clear() {
        super.clear();
        mItems.clear();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Article> collection) {
        super.addAll(collection);
        mItems.addAll(collection);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.listview_newsitem, parent, false);
        TextView textViewTopic = (TextView) rowView.findViewById(R.id.textview_topic);

        Article item = mItems.get(position);

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        textViewTopic.setText(format.format(item.PubDate) + " " + item.Title);

        return rowView;
    }


}
