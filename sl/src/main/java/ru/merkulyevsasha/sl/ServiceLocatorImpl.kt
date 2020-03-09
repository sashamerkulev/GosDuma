package ru.merkulyevsasha.sl

import android.content.Context
import ru.merkulyevsasha.aktcomments.AktCommentsInteractorImpl
import ru.merkulyevsasha.aktcomments.AktCommentsPresenterImpl
import ru.merkulyevsasha.aktdetails.AktDetailsPresenterImpl
import ru.merkulyevsasha.akts.AktsInteractorImpl
import ru.merkulyevsasha.akts.AktsPresenterImpl
import ru.merkulyevsasha.articlecomments.ArticleCommentsInteractorImpl
import ru.merkulyevsasha.articlecomments.ArticleCommentsPresenterImpl
import ru.merkulyevsasha.articledetails.ArticleDetailsPresenterImpl
import ru.merkulyevsasha.articles.ArticlesInteractorImpl
import ru.merkulyevsasha.articles.ArticlesPresenterImpl
import ru.merkulyevsasha.core.ArticleDistributor
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.core.domain.ArticleCommentsInteractor
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.domain.SetupInteractor
import ru.merkulyevsasha.core.domain.SourceInteractor
import ru.merkulyevsasha.core.domain.UsersInteractor
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.ArticleCommentsApiRepository
import ru.merkulyevsasha.core.repositories.ArticlesApiRepository
import ru.merkulyevsasha.core.repositories.NewsDatabaseRepository
import ru.merkulyevsasha.core.repositories.SetupApiRepository
import ru.merkulyevsasha.core.repositories.UsersApiRepository
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.core.routers.MainFragmentRouter
import ru.merkulyevsasha.coreandroid.providers.ResourceProviderImpl
import ru.merkulyevsasha.deputies.DeputiesInteractorImpl
import ru.merkulyevsasha.deputies.DeputiesPresenterImpl
import ru.merkulyevsasha.domain.ArticleDistributorImpl
import ru.merkulyevsasha.domain.SetupInteractorImpl
import ru.merkulyevsasha.domain.SourceInteractorImpl
import ru.merkulyevsasha.gdcore.AktDistributor
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.database.GdDatabaseRepository
import ru.merkulyevsasha.gdcore.domain.AktCommentsInteractor
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import ru.merkulyevsasha.gdcore.repositories.AktCommentsApiRepository
import ru.merkulyevsasha.gdcore.repositories.AktsApiRepository
import ru.merkulyevsasha.gdcore.repositories.DeputiesApiRepository
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter
import ru.merkulyevsasha.gddbrepository.database.GdDatabaseRepositoryImpl
import ru.merkulyevsasha.gddbrepository.database.GosdumaRoomDatabaseSourceCreator
import ru.merkulyevsasha.gddomain.AktDistributorImpl
import ru.merkulyevsasha.gdnetrepository.network.aktcomments.AktCommentsApiRepositoryImpl
import ru.merkulyevsasha.gdnetrepository.network.akts.AktsApiRepositoryImpl
import ru.merkulyevsasha.gdnetrepository.network.deputies.DeputiesApiRepositoryImpl
import ru.merkulyevsasha.netrepository.network.articlecomments.ArticleCommentsApiRepositoryImpl
import ru.merkulyevsasha.netrepository.network.articles.ArticlesApiRepositoryImpl
import ru.merkulyevsasha.netrepository.network.setup.SetupApiRepositoryImpl
import ru.merkulyevsasha.netrepository.network.users.UsersApiRepositoryImpl
import ru.merkulyevsasha.preferences.SettingsSharedPreferencesImpl
import ru.merkulyevsasha.userinfo.UserInfoPresenterImpl
import ru.merkulyevsasha.userinfo.UsersInteractorImpl

class ServiceLocatorImpl private constructor(context: Context) : GDServiceLocator {

    companion object {
        private var instance: GDServiceLocator? = null
        fun getInstance(context: Context): GDServiceLocator {
            if (instance == null) {
                instance = ServiceLocatorImpl(context)
            }
            return instance!!
        }
    }

    private val maps = HashMap<Any, Any>()

    init {
        val databaseSource = GosdumaRoomDatabaseSourceCreator.create(context, BuildConfig.DB_NAME)
        val prefs = SettingsSharedPreferencesImpl(context)
        val resourceProvider = ResourceProviderImpl(context)
        maps[KeyValueStorage::class.java] = prefs
        maps[SettingsSharedPreferences::class.java] = prefs
        maps[ResourceProvider::class.java] = resourceProvider
        maps[ArticleDistributor::class.java] = ArticleDistributorImpl(context, resourceProvider)
        maps[AktDistributor::class.java] = AktDistributorImpl(context, resourceProvider)
        maps[SetupApiRepository::class.java] = SetupApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[ArticlesApiRepository::class.java] = ArticlesApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[ArticleCommentsApiRepository::class.java] = ArticleCommentsApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[AktsApiRepository::class.java] = AktsApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[AktCommentsApiRepository::class.java] = AktCommentsApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[UsersApiRepository::class.java] = UsersApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[DeputiesApiRepository::class.java] = DeputiesApiRepositoryImpl(prefs, BuildConfig.API_URL, BuildConfig.DEBUG_MODE)
        maps[NewsDatabaseRepository::class.java] = GdDatabaseRepositoryImpl(databaseSource, prefs, BuildConfig.API_URL)
        maps[GdDatabaseRepository::class.java] = GdDatabaseRepositoryImpl(databaseSource, prefs, BuildConfig.API_URL)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(clazz: Class<T>): T {
        if (maps.containsKey(clazz)) {
            return maps[clazz] as T
        }
        when (clazz) {
            ArticlesInteractor::class.java -> maps[clazz] = ArticlesInteractorImpl(
                getArticlesApiRepository(),
                getPreferences(),
                getDatabaseRepository())
            AktsInteractor::class.java -> maps[clazz] = AktsInteractorImpl(
                getAktsApiRepository(),
                getSettingsSharedPreferences(),
                getGDDatabaseRepository())
            UsersInteractor::class.java -> maps[clazz] = UsersInteractorImpl(
                getUsersApiRepository(),
                getPreferences(),
                getDatabaseRepository())
            ArticleCommentsInteractor::class.java -> maps[clazz] = ArticleCommentsInteractorImpl(
                getArticlesApiRepository(),
                getArticleCommentsApiRepository(),
                getPreferences(),
                getDatabaseRepository())
            AktCommentsInteractor::class.java -> maps[clazz] = AktCommentsInteractorImpl(
                getAktsApiRepository(),
                getAktCommentsApiRepository(),
                getSettingsSharedPreferences(),
                getGDDatabaseRepository())
            SetupInteractor::class.java -> maps[clazz] = SetupInteractorImpl(
                getPreferences(),
                getSetupApiRepository(),
                getDatabaseRepository())
            SourceInteractor::class.java -> maps[clazz] = SourceInteractorImpl(
                getDatabaseRepository())
            DeputiesInteractor::class.java -> maps[clazz] = DeputiesInteractorImpl(
                get(DeputiesApiRepository::class.java))
            ArticleCommentsPresenterImpl::class.java -> maps[clazz] = ArticleCommentsPresenterImpl(
                get(ArticleCommentsInteractor::class.java),
                get(ArticlesInteractor::class.java),
                get(ArticleDistributor::class.java))
            ArticleDetailsPresenterImpl::class.java -> maps[clazz] = ArticleDetailsPresenterImpl(
                get(ArticlesInteractor::class.java),
                get(ArticleDistributor::class.java),
                get(MainActivityRouter::class.java))
            ArticlesPresenterImpl::class.java -> maps[clazz] = ArticlesPresenterImpl(
                get(ArticlesInteractor::class.java),
                get(ArticleDistributor::class.java),
                get(MainActivityRouter::class.java))
            UserInfoPresenterImpl::class.java -> maps[clazz] = UserInfoPresenterImpl(get(UsersInteractor::class.java), get(SourceInteractor::class.java))
            AktsPresenterImpl::class.java -> maps[clazz] = AktsPresenterImpl(
                get(AktsInteractor::class.java),
                get(AktDistributor::class.java),
                get(GDMainActivityRouter::class.java))
            AktDetailsPresenterImpl::class.java -> maps[clazz] = AktDetailsPresenterImpl(
                get(AktsInteractor::class.java),
                get(AktDistributor::class.java),
                get(GDMainActivityRouter::class.java))
            AktCommentsPresenterImpl::class.java -> maps[clazz] = AktCommentsPresenterImpl(
                get(AktCommentsInteractor::class.java),
                get(AktsInteractor::class.java),
                get(AktDistributor::class.java))
            DeputiesPresenterImpl::class.java -> maps[clazz] = DeputiesPresenterImpl(
                get(DeputiesInteractor::class.java),
                get(ResourceProvider::class.java))
        }
        return maps[clazz] as T
    }

    override fun <T> set(clazz: Class<T>, instance: Any) {
        maps[clazz] = instance
    }

    override fun <T> release(clazz: Class<T>) {
        if (maps.containsKey(clazz)) {
            maps.remove(clazz)
        }
    }

    override fun releaseAll() {
        maps.clear()
        instance = null
    }

    override fun addFragmentRouter(mainFragmentRouter: MainFragmentRouter) {
        maps[MainFragmentRouter::class.java] = mainFragmentRouter
    }

    override fun releaseFragmentRouter() {
        maps.remove(MainFragmentRouter::class.java)
    }

    override fun addMainRouter(mainActivityRouter: MainActivityRouter) {
        maps[MainActivityRouter::class.java] = mainActivityRouter
    }

    override fun releaseMainRouter() {
        maps.remove(MainActivityRouter::class.java)
    }

    override fun addGDFragmentRouter(mainFragmentRouter: GDMainFragmentRouter) {
        maps[GDMainFragmentRouter::class.java] = mainFragmentRouter
    }

    override fun releaseGDFragmentRouter() {
        maps.remove(GDMainFragmentRouter::class.java)
    }

    override fun addGDMainRouter(mainActivityRouter: GDMainActivityRouter) {
        maps[GDMainActivityRouter::class.java] = mainActivityRouter
    }

    override fun releaseGDMainRouter() {
        maps.remove(GDMainActivityRouter::class.java)
    }

    private fun getArticlesApiRepository(): ArticlesApiRepository {
        return maps[ArticlesApiRepository::class.java] as ArticlesApiRepository
    }

    private fun getArticleCommentsApiRepository(): ArticleCommentsApiRepository {
        return maps[ArticleCommentsApiRepository::class.java] as ArticleCommentsApiRepository
    }

    private fun getAktsApiRepository(): AktsApiRepository {
        return maps[AktsApiRepository::class.java] as AktsApiRepository
    }

    private fun getAktCommentsApiRepository(): AktCommentsApiRepository {
        return maps[AktCommentsApiRepository::class.java] as AktCommentsApiRepository
    }

    private fun getUsersApiRepository(): UsersApiRepository {
        return maps[UsersApiRepository::class.java] as UsersApiRepository
    }

    private fun getSetupApiRepository(): SetupApiRepository {
        return maps[SetupApiRepository::class.java] as SetupApiRepository
    }

    private fun getDatabaseRepository(): NewsDatabaseRepository {
        return maps[NewsDatabaseRepository::class.java] as NewsDatabaseRepository
    }

    private fun getGDDatabaseRepository(): GdDatabaseRepository {
        return maps[GdDatabaseRepository::class.java] as GdDatabaseRepository
    }

    private fun getPreferences(): KeyValueStorage {
        return maps[KeyValueStorage::class.java] as KeyValueStorage
    }

    private fun getSettingsSharedPreferences(): SettingsSharedPreferences {
        return maps[SettingsSharedPreferences::class.java] as SettingsSharedPreferences
    }
}