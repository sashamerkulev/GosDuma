package ru.merkulyevsasha.gosduma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.ListData;
import ru.merkulyevsasha.gosduma.ui.ListViewListDataAdapter;


public class ListActivity extends BaseActivity {

    private HashMap<Integer, String> mListDataTableName = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initSupportActionBarWithBackButton(R.id.list_toolbar);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);
        final String name = intent.getStringExtra("name");
        setTitle(name);

        mListDataTableName.put(R.id.nav_comittee, DatabaseHelper.COMMITTEE_TABLE_NAME);
        mListDataTableName.put(R.id.nav_blocks, DatabaseHelper.BLOCKS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_otras, DatabaseHelper.OTRAS_TABLE_NAME);
        mListDataTableName.put(R.id.nav_reg, DatabaseHelper.REG_TABLE_NAME);
        mListDataTableName.put(R.id.nav_fed, DatabaseHelper.FED_TABLE_NAME);
        mListDataTableName.put(R.id.nav_stad, DatabaseHelper.STAD_TABLE_NAME);
        mListDataTableName.put(R.id.nav_inst, DatabaseHelper.INST_TABLE_NAME);

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        List<ListData> data = db.selectAll(mListDataTableName.get(id));

        ListView mListView = (ListView) findViewById(R.id.listview_listdata);
        ListViewListDataAdapter mAdapter = new ListViewListDataAdapter(this, new ArrayList<ListData>());
        mListView.setAdapter(mAdapter);
        mAdapter.clear();
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();

    }


}