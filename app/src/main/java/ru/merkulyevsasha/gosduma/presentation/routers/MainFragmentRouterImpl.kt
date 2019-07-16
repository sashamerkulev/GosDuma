package ru.merkulyevsasha.gosduma.presentation.routers

import androidx.fragment.app.FragmentManager
import ru.merkulyevsasha.articles.ArticlesFragment
import ru.merkulyevsasha.core.routers.MainFragmentRouter
import ru.merkulyevsasha.coreandroid.routers.BaseRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter
import ru.merkulyevsasha.gosduma.R
import ru.merkulyevsasha.userinfo.UserInfoFragment

class MainFragmentRouterImpl(fragmentManager: FragmentManager) : BaseRouter(R.id.mainContainer, fragmentManager),
    MainFragmentRouter, GDMainFragmentRouter {

    override fun showArticles() {
        val tag = ArticlesFragment.TAG
        val fragment = findOrCreateFragment(tag) { ArticlesFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

    override fun showUserActivities() {
//        val tag = UserActivitiesFragment.TAG
//        val fragment = findOrCreateFragment(tag) { UserActivitiesFragment.newInstance() }
//        replaceFragment(tag, fragment)
    }

    override fun showUserInfo() {
        val tag = UserInfoFragment.TAG
        val fragment = findOrCreateFragment(tag) { UserInfoFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

}