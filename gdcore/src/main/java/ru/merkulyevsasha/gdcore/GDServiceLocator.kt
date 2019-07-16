package ru.merkulyevsasha.gdcore

import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter

interface GDServiceLocator: ServiceLocator {
    fun addGDFragmentRouter(mainFragmentRouter: GDMainFragmentRouter)
    fun releaseGDFragmentRouter()
    fun addGDMainRouter(mainActivityRouter: GDMainActivityRouter)
    fun releaseGDMainRouter()
}