package ru.merkulyevsasha.gosduma.presentation.services;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by sasha_merkulev on 10.07.2017.
 */

public class GDJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        return new GDJob();
    }
}
