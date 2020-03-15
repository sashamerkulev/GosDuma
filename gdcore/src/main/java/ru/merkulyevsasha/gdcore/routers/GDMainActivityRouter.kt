package ru.merkulyevsasha.gdcore.routers

import ru.merkulyevsasha.core.routers.MainActivityRouter

interface GDMainActivityRouter : MainActivityRouter{
    fun showDeputyDetails(deputyId: Int)
    fun showLawDetails(lawId: Int)
    fun showAktDetails(articleId: Int)
    fun showAktComments(articleId: Int)
}