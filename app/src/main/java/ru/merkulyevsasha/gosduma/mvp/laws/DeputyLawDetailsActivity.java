package ru.merkulyevsasha.gosduma.mvp.laws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.merkulyevsasha.gosduma.R;

public class DeputyLawDetailsActivity extends BaseLawDetailsActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivity(savedInstanceState);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                final StringBuilder message = new StringBuilder();

                message.append(mLaw.type);
                message.append("\n");
                message.append(mLaw.getLawNameWithNumberAndDate());
                message.append("\n");
                if (mLaw.comments != null && !mLaw.comments.isEmpty()) {
                    message.append("Комментарий: ");
                    message.append(mLaw.comments);
                    message.append("\n");
                }
                if (mLaw.lastEventSolution != null && !mLaw.lastEventSolution.isEmpty()){
                    message.append("Решение: ");
                    message.append(mLaw.lastEventSolution);
                    message.append("\n");
                }
                if (mLaw.responsibleName != null && !mLaw.responsibleName.isEmpty()){
                    message.append("Отв.комитет: ");
                    message.append(mLaw.responsibleName);
                    message.append("\n");
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_using)));
            }
        });
    }

}
