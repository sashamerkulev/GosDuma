package ru.merkulyevsasha.gosduma;

import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class BaseActivity extends AppCompatActivity {

    private boolean isLarge() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    protected boolean isLargeLandscape(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && isLarge()) {
            return true;
        }
        return false;
    }

    protected void initSupportActionBarWithBackButton(int resId){
        Toolbar toolbar = (Toolbar) findViewById(resId);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setTextToTextViewOrGone(String text, TextView textView){
        if (text == null || text.isEmpty()){
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(text);
        }
    }

}
