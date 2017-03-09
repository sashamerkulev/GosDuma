package ru.merkulyevsasha.gosduma.presentation.deputydetails;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.merkulyevsasha.gosduma.BaseActivity;
import ru.merkulyevsasha.gosduma.ui.DialogHelper;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.models.Deputy;
import ru.merkulyevsasha.gosduma.models.Law;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.LawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsView;
import ru.merkulyevsasha.gosduma.presentation.lawdetails.DeputyLawDetailsActivity;
import ru.merkulyevsasha.gosduma.presentation.laws.LawsRecyclerViewAdapter;
import ru.merkulyevsasha.gosduma.ui.UiUtils;

import static ru.merkulyevsasha.gosduma.ui.UiUtils.setTextToTextViewOrGone;

@SuppressWarnings("WeakerAccess")
public class DeputyDetailsActivity extends BaseActivity
    implements
        LawsView.OnLawClickListener
        , MenuItem.OnMenuItemClickListener
        , SearchView.OnQueryTextListener
    , LawsView
{
    public final static String KEY_DEPUTY = "DEPUTY";
    public final static String KEY_POSITION = "POSITION";
    private final static String KEY_MENUITEMSVISIBLE = "MENUITEMSVISIBLE";

    @BindView(R.id.collapsingToolbar)
    public
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.appbarlayout)
    public
    AppBarLayout mAppbarLayout;

    @BindView(R.id.textview_deputy_name)
    public
    TextView mDeputyName;
    @BindView(R.id.textview_position)
    public
    TextView mDeputyPosition;
    @BindView(R.id.textview_deputy_fractionName)
    public
    TextView mFractionName;
    @BindView(R.id.textview_deputy_fractionRole)
    public
    TextView mFractionRole;
    @BindView(R.id.textview_deputy_ranks)
    public
    TextView mDeputyRanks;

    @BindView(R.id.fab)
    public FloatingActionButton mFab;

    @BindView(R.id.recyclerview_laws)
    public RecyclerView mRecyclerView;

    @BindView(R.id.layout_empty_search)
    public LinearLayout mEmptyLayout;

    private LawsRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private Deputy mDeputy;
    private DeputyLawsPresenter mPresenter;

    private MenuItem mSearchItem;
    private MenuItem mSortItem;

    private int mPosition = -1;
    private boolean mMenuItemsVisible = false;

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        Bundle state = mPresenter.getState();
        if (state != null){
            outState.putAll(state);
        }
        outState.putInt(KEY_POSITION, mLayoutManager.findFirstVisibleItemPosition());
        outState.putBoolean(KEY_MENUITEMSVISIBLE, mMenuItemsVisible);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UiUtils.isLargeLandscape(this)) {
            finish();
            return;
        }

        mPresenter = new DeputyLawsPresenter(this, this);

        setContentView(R.layout.activity_deputy_details);
        ButterKnife.bind(this);

        initSupportActionBarWithBackButton(R.id.deputydetails_toolbar);

        Intent intent = getIntent();
        mDeputy = intent.getParcelableExtra(KEY_DEPUTY);
        setTitle("");

        if (savedInstanceState != null){
            mPresenter.restoreState(savedInstanceState);
            mPosition = savedInstanceState.getInt(KEY_POSITION);
            mMenuItemsVisible = savedInstanceState.getBoolean(KEY_MENUITEMSVISIBLE);
        }

        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    setCollapsingToolbarTitleEnabled(true);
                } else if (verticalOffset == 0) {
                    // Expanded
                    setCollapsingToolbarTitleEnabled(false);
                } else {
                    // Somewhere in between
                    setCollapsingToolbarTitleEnabled(false);
                }
            }
        });

        mDeputyPosition.setText(mDeputy.getPositionWithStartAndEndDates());

        mDeputyName.setText(mDeputy.getNameWithBirthday());

        setTextToTextViewOrGone(mDeputy.getRanksWithDegrees(), mDeputyRanks);

        mFractionName.setText(mDeputy.fractionName);
        mFractionRole.setText(mDeputy.fractionRole + " " + mDeputy.fractionRegion);

        List<Law> items = mPresenter.getDeputyLaws(mDeputy.id);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LawsRecyclerViewAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);
        showData(items.size() > 0);

        if (savedInstanceState != null && mMenuItemsVisible) {
            mAppbarLayout.setExpanded(false);
        }

        if (mPosition > 0){
            mRecyclerView.scrollToPosition(mPosition);
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                final StringBuilder message = new StringBuilder();

                message.append(mDeputy.getNameWithBirthday());
                message.append("\n");
                message.append(mDeputy.getPositionWithStartAndEndDates());
                message.append("\n");
                if (!mDeputy.getRanksWithDegrees().isEmpty()) {
                    message.append(mDeputy.getRanksWithDegrees());
                    message.append("\n");
                }
                message.append(mDeputy.fractionName);
                message.append("\n");
                message.append(mDeputy.fractionRole);
                if (!mDeputy.fractionRegion.isEmpty()){
                    message.append(" ");
                    message.append(mDeputy.fractionRegion);
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
            }
        });

    }

    private void setCollapsingToolbarTitleEnabled(boolean enable) {
        if (enable) {
            setTitle(mDeputy.name);
            mCollapsingToolbar.setTitleEnabled(true);
            mDeputyName.setTextColor(Color.TRANSPARENT);
            mDeputyPosition.setTextColor(Color.TRANSPARENT);
            mDeputyRanks.setTextColor(Color.TRANSPARENT);
            mFractionName.setTextColor(Color.TRANSPARENT);
            mFractionRole.setTextColor(Color.TRANSPARENT);
            visibleMenuItems(true);
        } else {
            setTitle("");
            mCollapsingToolbar.setTitleEnabled(false);

            mDeputyName.setTextColor(ContextCompat.getColor(DeputyDetailsActivity.this, R.color.textColorPrimary));
            mDeputyPosition.setTextColor(ContextCompat.getColor(DeputyDetailsActivity.this, R.color.textColorPrimary));
            mDeputyRanks.setTextColor(ContextCompat.getColor(DeputyDetailsActivity.this, R.color.textColorPrimary));
            mFractionName.setTextColor(ContextCompat.getColor(DeputyDetailsActivity.this, R.color.textColorPrimary));
            mFractionRole.setTextColor(ContextCompat.getColor(DeputyDetailsActivity.this, R.color.textColorPrimary));

            visibleMenuItems(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.deputy, menu);

        mSearchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(this);

        mSortItem = menu.findItem(R.id.action_sort);
        mSortItem.setOnMenuItemClickListener(this);

        return true;
    }

    private void visibleMenuItems(boolean visible){
        mMenuItemsVisible = visible;

        if (mSearchItem != null)
            mSearchItem.setVisible(visible);
        if (mSortItem != null)
            mSortItem.setVisible(visible);
    }


    @Override
    public void onLawClick(Law law) {
        Intent activityIntent = new Intent(this, DeputyLawDetailsActivity.class);
        activityIntent.putExtra(LawDetailsActivity.KEY_LAW, law);
        startActivity(activityIntent);
    }

    private void showData(boolean show){
        if (show) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showData(List<Law> items) {
        mAdapter.mItems = items;
        mAdapter.notifyDataSetChanged();
        showData(items.size() > 0);
    }

    @Override
    public void showDataEmptyMessage() {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()){
            mPresenter.search(newText);
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.action_sort) {

            int sortDialogType = mPresenter.getSortDialogType();
            if (sortDialogType == DialogHelper.IDD_LAWS_SORT) {
                DialogHelper.getLawSortDialog(this,
                        mPresenter.getCurrentSortIndexValue().get(0),
                        new DialogHelper.DialogClickListener() {
                            @Override
                            public void onClick(List<Integer> selectItemsIndex) {
                                mPresenter.sort(mPresenter.getCurrentSortIndexValue(), selectItemsIndex);
                            }
                        }
                ).show();
            }

            return false;
        }

        return false;
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }
}
