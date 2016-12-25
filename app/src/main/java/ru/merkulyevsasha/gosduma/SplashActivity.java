package ru.merkulyevsasha.gosduma;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.merkulyevsasha.gosduma.db.DatabaseHelper;


public class SplashActivity extends AppCompatActivity {

    private final static int MAX_PROGRESS = 200;

    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);
        mProgress.setMax(MAX_PROGRESS);

        CopyDbTask task = new CopyDbTask();
        task.execute();

    }

    private void startApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    class CopyDbTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            AssetManager assetManager = getAssets();
            try {
                File fileDb = new File(getFilesDir(), DatabaseHelper.DATABASE_NAME);
                if (!fileDb.exists()) {
                    int progress = 1;
                    InputStream in = assetManager.open(DatabaseHelper.DATABASE_NAME);
                    FileOutputStream out = new FileOutputStream(fileDb);

                    long stepProgressSize = in.available() / MAX_PROGRESS;

                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len;

                    long readInputSize = 0;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);

                        readInputSize = readInputSize + len;
                        if (readInputSize >= stepProgressSize) {
                            readInputSize = 0;
                            try {
                                Thread.sleep(10);
                                publishProgress(progress++);
                            } catch(Exception e) {
                            }
                        }
                    }

                    in.close();
                    out.close();
                }

            } catch (IOException e) {
                FirebaseCrash.report(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startApp();
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgress.setProgress(values[0]);
        }
    }


}

