package ru.merkulyevsasha.gosduma

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.FragmentManager
import com.facebook.stetho.Stetho

import com.google.android.gms.ads.MobileAds
import com.squareup.leakcanary.LeakCanary
import ru.merkulyevsasha.core.ServiceLocator

class GosDumaApp : Application() {

    private lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        registerActivityLifecycleCallbacks(LifeCycleCallbacks())

        MobileAds.initialize(this, getString(R.string.APP_ID))

        if (BuildConfig.DEBUG_MODE) {
            Stetho.initializeWithDefaults(this)

            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build())

            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                //.detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build())
        }
    }

    inner class LifeCycleCallbacks : FragmentManager.FragmentLifecycleCallbacks(), ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }
    }

}
