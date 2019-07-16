package ru.merkulyevsasha.sl

import android.content.Context
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.core.routers.MainFragmentRouter
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter

class ServiceLocatorImpl private constructor(context: Context) : ServiceLocator, GDServiceLocator {

    companion object {
        private var instance: ServiceLocator? = null
        fun getInstance(context: Context): ServiceLocator {
            if (instance == null) {
                instance = ServiceLocatorImpl(context)
            }
            return instance!!
        }
    }

    private val maps = HashMap<Any, Any>()

    init {

    }

    override fun <T> get(clazz: Class<T>): T {
        TODO()
    }

    override fun <T> set(clazz: Class<T>, instance: Any) {
        maps[clazz] = instance
    }

    override fun <T> release(clazz: Class<T>) {
        if (maps.containsKey(clazz)) {
            maps.remove(clazz)
        }
    }

    override fun releaseAll() {
        maps.clear()
        instance = null
    }

    override fun addFragmentRouter(mainFragmentRouter: MainFragmentRouter) {
        maps[MainFragmentRouter::class.java] = mainFragmentRouter
    }

    override fun releaseFragmentRouter() {
        maps.remove(MainFragmentRouter::class.java)
    }

    override fun addMainRouter(mainActivityRouter: MainActivityRouter) {
        maps[MainActivityRouter::class.java] = mainActivityRouter
    }

    override fun releaseMainRouter() {
        maps.remove(MainActivityRouter::class.java)
    }

    override fun addGDFragmentRouter(mainFragmentRouter: GDMainFragmentRouter) {
        maps[GDMainFragmentRouter::class.java] = mainFragmentRouter
    }

    override fun releaseGDFragmentRouter() {
        maps.remove(GDMainFragmentRouter::class.java)
    }

    override fun addGDMainRouter(mainActivityRouter: GDMainActivityRouter) {
        maps[GDMainActivityRouter::class.java] = mainActivityRouter
    }

    override fun releaseGDMainRouter() {
        maps.remove(GDMainActivityRouter::class.java)
    }

}