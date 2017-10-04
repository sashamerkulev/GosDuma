package ru.merkulyevsasha.gosduma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.presentation.main.MainActivity;


public class SplashActivity extends AppCompatActivity {

    private final static int MAX_PROGRESS = 200;
    private final static int DB_VERSION = 3;

    private ProgressBar progress;
    private CopyDbTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);
        progress = findViewById(R.id.splash_screen_progress_bar);
        progress.setMax(MAX_PROGRESS);

        task = new CopyDbTask();
        task.execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task != null) task.cancel(false);
    }

    private void startApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    private class CopyDbTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            AssetManager assetManager = getAssets();
            try {
                File fileDb = new File(getFilesDir(), DatabaseHelper.DATABASE_NAME);
				boolean hasNewVersion = !fileDb.exists();
				if (fileDb.exists()){
					try{
						SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(fileDb.getAbsolutePath(), null);
						if (DB_VERSION > db.getVersion()){
							hasNewVersion = true;
						}
					} catch(Exception e){
						e.printStackTrace();
					}
				}
				
                if (hasNewVersion) {
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
                                e.printStackTrace();
                                FirebaseCrash.report(e);
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
            if (!isCancelled()) {
                startApp();
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }
    }


}

