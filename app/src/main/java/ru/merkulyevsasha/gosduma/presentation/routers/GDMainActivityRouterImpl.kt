package ru.merkulyevsasha.gosduma.presentation.routers

import androidx.fragment.app.FragmentManager
import ru.merkulyevsasha.aktcomments.AktCommentsFragment
import ru.merkulyevsasha.aktdetails.AktDetailsFragment
import ru.merkulyevsasha.articlecomments.ArticleCommentsFragment
import ru.merkulyevsasha.articledetails.ArticleDetailsFragment
import ru.merkulyevsasha.coreandroid.routers.BaseRouter
import ru.merkulyevsasha.deputydetails.DeputyDetailsFragment
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

    override fun showAktDetails(articleId: Int) {
        val tag = AktDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { AktDetailsFragment.newInstance(articleId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showArticleComments(articleId: Int) {
        val tag = ArticleCommentsFragment.TAG
        val fragment = findOrCreateFragment(tag) { ArticleCommentsFragment.newInstance(articleId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showSourceArticles(sourceId: String, sourceName: String) {

    }

    override fun showAktComments(articleId: Int) {
        val tag = AktCommentsFragment.TAG
        val fragment = findOrCreateFragment(tag) { AktCommentsFragment.newInstance(articleId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showDeputyDetails(deputyId: Int) {
        val tag = DeputyDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { DeputyDetailsFragment.newInstance(deputyId) }
        replaceFragment(tag, fragment, true)
    }

    override fun showLawDetails(lawId: Int) {
        val tag = LawDetailsFragment.TAG
        val fragment = findOrCreateFragment(tag) { LawDetailsFragment.newInstance() }
        replaceFragment(tag, fragment, true)
    }

}