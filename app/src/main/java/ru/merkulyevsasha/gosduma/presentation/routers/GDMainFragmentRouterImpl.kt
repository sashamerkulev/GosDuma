package ru.merkulyevsasha.gosduma.presentation.routers

import androidx.fragment.app.FragmentManager
import ru.merkulyevsasha.akts.AktsFragment
import ru.merkulyevsasha.articles.ArticlesFragment
import ru.merkulyevsasha.coreandroid.routers.BaseRouter
import ru.merkulyevsasha.deputies.DeputiesFragment
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter
import ru.merkulyevsasha.gosduma.R
import ru.merkulyevsasha.laws.LawsFragment
import ru.merkulyevsasha.userinfo.UserInfoFragment

class GDMainFragmentRouterImpl(fragmentManager: FragmentManager) : BaseRouter(R.id.mainContainer, fragmentManager),
    GDMainFragmentRouter {

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

    override fun showDeputies() {
        val tag = DeputiesFragment.TAG
        val fragment = findOrCreateFragment(tag) { DeputiesFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

    override fun showLaws() {
        val tag = LawsFragment.TAG
        val fragment = findOrCreateFragment(tag) { LawsFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

    override fun showAkts() {
        val tag = AktsFragment.TAG
        val fragment = findOrCreateFragment(tag) { AktsFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

    override fun showMore() {
//        val tag = MenuFragment.TAG
//        val fragment = findOrCreateFragment(tag) { MenuFragment.newInstance() }
//        replaceFragment(tag, fragment)
    }

}