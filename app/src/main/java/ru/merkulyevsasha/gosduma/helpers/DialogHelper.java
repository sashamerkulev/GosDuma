package ru.merkulyevsasha.gosduma.helpers;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.presentation.deputies.DeputiesPresenter;

public class DialogHelper {

    public final static int IDD_DEPUTY_SORT = 1;
    public final static int IDD_DEPUTY_FILTER = 2;
    public final static int IDD_LAWS_SORT = 3;
    public final static int IDD_DEPUTY_REQUEST_SORT = 4;


    public static Dialog getLawSortDialog(final Activity context, int currentItemIndex, final DialogItemClickListener listener){

        final String[] sortItems = {
                context.getString(R.string.item_sort_lawname),
                context.getString(R.string.item_sort_number),
                context.getString(R.string.item_sort_date)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_sort);

        builder.setSingleChoiceItems(sortItems, currentItemIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        dialog.dismiss();

                        listener.onClick(item);
                    }
                });

        return builder.create();

    }

    public static Dialog getDeputyRequestsSortDialog(final Activity context, int currentItemIndex, final DialogItemClickListener listener){

        final String[] sortItems = {
                context.getString(R.string.item_sort_lawname),
                context.getString(R.string.item_sort_date),
                context.getString(R.string.item_sort_initiator)};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_sort);

        builder.setSingleChoiceItems(sortItems, currentItemIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        dialog.dismiss();

                        listener.onClick(item);
                    }
                });

        return builder.create();

    }


    public static Dialog getDeputyFilterDialog(final Activity context, List<Integer> filterSettings, final DialogItemsClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_filter);

        View view = context.getLayoutInflater().inflate(R.layout.dialog_deputy_filter, null); // Получаем layout по его ID
        builder.setView(view);
        final RadioButton rb_deputy = (RadioButton)view.findViewById(R.id.radiobox_deputy_gd);
        final RadioButton rb_member = (RadioButton)view.findViewById(R.id.radiobox_member);
        final RadioButton rb_working = (RadioButton)view.findViewById(R.id.radiobox_working);
        final RadioButton rb_not_working = (RadioButton)view.findViewById(R.id.radiobox_not_working);

        final int deputy =  filterSettings.get(0);

        rb_deputy.setChecked(deputy == DeputiesPresenter.DEPUTY_INDEX);
        rb_member.setChecked(deputy == DeputiesPresenter.MEMBER_INDEX);

        final int working =  filterSettings.get(1);
        rb_working.setChecked(working == DeputiesPresenter.WORKING_INDEX);
        rb_not_working.setChecked(working == DeputiesPresenter.NOT_WORKING_INDEX);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            public void onClick(DialogInterface dialog, int whichButton) {
                List<Integer> newFilterSettings = new ArrayList<>();
                if (rb_deputy.isChecked()){
                    newFilterSettings.add(DeputiesPresenter.DEPUTY_INDEX);
                } else {
                    if (rb_member.isChecked()) {
                        newFilterSettings.add(DeputiesPresenter.MEMBER_INDEX);
                    }
                }
                if (rb_working.isChecked()){
                    newFilterSettings.add(DeputiesPresenter.WORKING_INDEX);
                } else {
                    if (rb_not_working.isChecked()) {
                        newFilterSettings.add(DeputiesPresenter.NOT_WORKING_INDEX);
                    }
                }
                dialog.dismiss();
                listener.onClick(newFilterSettings);
            }
        });

        return builder.create();
    }

    public interface DialogItemClickListener{
        void onClick(int selectItemsIndex);
    }

    public interface DialogItemsClickListener {
        void onClick(List<Integer> selectItemsIndex);
    }

}
