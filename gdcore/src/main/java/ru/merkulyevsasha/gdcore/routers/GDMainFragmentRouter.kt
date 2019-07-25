package ru.merkulyevsasha.gdcore.routers

import ru.merkulyevsasha.core.routers.MainFragmentRouter

interface GDMainFragmentRouter : MainFragmentRouter{
    fun showDeputies()
    fun showLaws()
    fun showAkts()
    fun showDeputyRequests()
    fun showMore()
}