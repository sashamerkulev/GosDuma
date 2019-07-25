package ru.merkulyevsasha.gosduma.presentation.routers

import androidx.fragment.app.FragmentManager
import ru.merkulyevsasha.articlecomments.ArticleCommentsFragment
import ru.merkulyevsasha.articledetails.ArticleDetailsFragment
import ru.merkulyevsasha.coreandroid.routers.BaseRouter
import ru.merkulyevsasha.deputydetails.DeputyDetailsFragment
import ru.merkulyevsasha.deputyrequestdetails.DeputyRequestDetailsFragment
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gosduma.R
import ru.merkulyevsasha.gosduma.presentation.main.MainFragment
import ru.merkulyevsasha.lawdetails.LawDetailsFragment

class GDMainActivityRouterImpl(fragmentManager: FragmentManager) : BaseRouter(R.id.container, fragmentManager),
    GDMainActivityRouter {

    override fun showMain() {
        val tag = MainFragment.TAG
        val fragment = findOrCreateFragment(tag) { MainFragment.newInstance() }
        replaceFragment(tag, fragment)
    }

    override fun showArticleDetails(articleId: Int) {
        val tag = ArticleDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { ArticleDetailsFragment.newInstance(articleId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showArticleComments(articleId: Int) {
        val tag = ArticleCommentsFragment.TAG
        val fragment = findOrCreateFragment(tag) { ArticleCommentsFragment.newInstance(articleId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showDeputyDetails() {
        val tag = DeputyDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { DeputyDetailsFragment.newInstance() }
        replaceFragment(tag, fragment, true)
    }

    override fun showLawDetails() {
        val tag = LawDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { LawDetailsFragment.newInstance() }
        replaceFragment(tag, fragment, true)
    }

    override fun showDeputyRequestDetails() {
        val tag = DeputyRequestDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { DeputyRequestDetailsFragment.newInstance() }
        replaceFragment(tag, fragment, true)
    }

}