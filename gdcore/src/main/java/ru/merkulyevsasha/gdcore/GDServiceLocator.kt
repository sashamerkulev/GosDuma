package ru.merkulyevsasha.gdcore

import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter

interface GDServiceLocator {
    fun addGDFragmentRouter(mainFragmentRouter: GDMainFragmentRouter)
    fun releaseGDFragmentRouter()
    fun addGDMainRouter(mainActivityRouter: GDMainActivityRouter)
    fun releaseGDMainRouter()
}