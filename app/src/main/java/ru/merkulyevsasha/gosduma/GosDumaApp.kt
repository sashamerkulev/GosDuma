package ru.merkulyevsasha.gosduma

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import com.squareup.leakcanary.LeakCanary
import ru.merkulyevsasha.core.RequireServiceLocator
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.RequireGDServiceLocator
import ru.merkulyevsasha.gosduma.presentation.main.MainActivity
import ru.merkulyevsasha.gosduma.presentation.main.MainFragment
import ru.merkulyevsasha.gosduma.presentation.routers.MainActivityRouterImpl
import ru.merkulyevsasha.gosduma.presentation.routers.MainFragmentRouterImpl
import ru.merkulyevsasha.sl.ServiceLocatorImpl

class GosDumaApp : Application() {

    private lateinit var serviceLocator: GDServiceLocator

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

        override fun onFragmentCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
            if (fragment.javaClass.simpleName == MainFragment::class.java.simpleName) {
                serviceLocator.addFragmentRouter(MainFragmentRouterImpl(fragment.childFragmentManager))
            }
            if (fragment is RequireServiceLocator) {
                fragment.setServiceLocator(serviceLocator)
            }
            if (fragment is RequireGDServiceLocator) {
                fragment.setGDServiceLocator(serviceLocator)
            }
            super.onFragmentCreated(fm, fragment, savedInstanceState)
        }

        override fun onFragmentDestroyed(fm: FragmentManager, fragment: Fragment) {
            if (fragment.javaClass.simpleName == MainFragment::class.java.simpleName) {
                serviceLocator.releaseFragmentRouter()
                serviceLocator.releaseGDFragmentRouter()
            }
            super.onFragmentDestroyed(fm, fragment)
        }

        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.let { activityInstance ->
                if (activityInstance.javaClass.simpleName == MainActivity::class.java.simpleName) {
                    val supportFragmentManager = (activity as AppCompatActivity).supportFragmentManager
                    supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                    serviceLocator.releaseMainRouter()
                    serviceLocator.releaseGDMainRouter()
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let { activityInstance ->
                if (activityInstance.javaClass.simpleName == MainActivity::class.java.simpleName) {
                    val supportFragmentManager = (activity as AppCompatActivity).supportFragmentManager
                    supportFragmentManager.registerFragmentLifecycleCallbacks(this, true)
                    serviceLocator = ServiceLocatorImpl.getInstance(this@GosDumaApp)
                    serviceLocator.addMainRouter(MainActivityRouterImpl(supportFragmentManager))
                    serviceLocator.addGDMainRouter(MainActivityRouterImpl(supportFragmentManager))
                }
                if (activityInstance is RequireServiceLocator) {
                    activityInstance.setServiceLocator(serviceLocator)
                }
                if (activityInstance is RequireGDServiceLocator) {
                    activityInstance.setGDServiceLocator(serviceLocator)
                }
            }
        }
    }

}
