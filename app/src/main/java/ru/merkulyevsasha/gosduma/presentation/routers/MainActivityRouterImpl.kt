package ru.merkulyevsasha.gosduma.presentation.routers

import androidx.fragment.app.FragmentManager
import ru.merkulyevsasha.articlecomments.ArticleCommentsFragment
import ru.merkulyevsasha.articledetails.ArticleDetailsFragment
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.coreandroid.routers.BaseRouter
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gosduma.R
import ru.merkulyevsasha.gosduma.presentation.main.MainFragment

class MainActivityRouterImpl(fragmentManager: FragmentManager) : BaseRouter(R.id.container, fragmentManager),
    MainActivityRouter, GDMainActivityRouter {

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

}