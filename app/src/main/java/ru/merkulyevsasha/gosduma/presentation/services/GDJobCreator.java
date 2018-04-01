package ru.merkulyevsasha.gosduma.presentation.services;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractor;

/**
 * Created by sasha_merkulev on 10.07.2017.
 */

public class GDJobCreator implements JobCreator {

    private final NewsServiceInteractor inter;

    @Inject
    GDJobCreator(NewsServiceInteractor inter){
        this.inter = inter;
    }

    @Override
    public Job create(@NonNull String tag) {
        return new GDJob(inter);
    }
}
