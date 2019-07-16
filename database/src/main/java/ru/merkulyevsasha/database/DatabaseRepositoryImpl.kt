package ru.merkulyevsasha.database

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Single
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.core.repositories.DatabaseRepository
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import java.util.*

class DatabaseRepositoryImpl(private val context: Context, private val preferences: SettingsSharedPreferences) : DatabaseRepository {
    override fun removeOldNotUserActivityArticles(cleanDate: Date) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun removeOldUserActivityArticles(cleanDate: Date) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticle(articleId: Int): Single<Article> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticles(): Single<List<Article>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticleComments(articleId: Int): Single<List<ArticleComment>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun saveRssSources(sources: List<RssSource>) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getRssSources(): List<RssSource> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRssSources() {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun addOrUpdateArticles(articles: List<Article>) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArticle(article: Article) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserActivityArticles(): Single<List<Article>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun addOrUpdateArticleComments(comments: List<ArticleComment>) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArticleComment(comment: ArticleComment, commentsCount: Int) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun searchArticles(searchText: String, byUserActivities: Boolean): Single<List<Article>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}