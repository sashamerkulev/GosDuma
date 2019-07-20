package ru.merkulyevsasha.sl

import android.content.Context
import androidx.room.Room
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.core.domain.ArticleCommentsInteractor
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.domain.SetupInteractor
import ru.merkulyevsasha.core.domain.UsersInteractor
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.core.repositories.ArticleCommentsApiRepository
import ru.merkulyevsasha.core.repositories.ArticlesApiRepository
import ru.merkulyevsasha.core.repositories.DatabaseRepository
import ru.merkulyevsasha.core.repositories.SetupApiRepository
import ru.merkulyevsasha.core.repositories.UsersApiRepository
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.core.routers.MainFragmentRouter
import ru.merkulyevsasha.coreandroid.providers.ResourceProviderImpl
import ru.merkulyevsasha.gddata.GosdumaDatabaseSourceImpl
import ru.merkulyevsasha.data.database.DatabaseRepositoryImpl
import ru.merkulyevsasha.data.network.articles.ArticlesApiRepositoryImpl
import ru.merkulyevsasha.data.network.comments.ArticleCommentsApiRepositoryImpl
import ru.merkulyevsasha.data.network.setup.SetupApiRepositoryImpl
import ru.merkulyevsasha.data.network.users.UsersApiRepositoryImpl
import ru.merkulyevsasha.gddatabase.data.GosdumaRoomDatabase
import ru.merkulyevsasha.domain.ArticleCommentsInteractorImpl
import ru.merkulyevsasha.domain.ArticlesInteractorImpl
import ru.merkulyevsasha.domain.NewsDistributorImpl
import ru.merkulyevsasha.domain.SetupInteractorImpl
import ru.merkulyevsasha.domain.UsersInteractorImpl
import ru.merkulyevsasha.domain.mappers.SourceNameMapper
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter
import ru.merkulyevsasha.preferences.SettingsSharedPreferencesImpl

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
        val roomDatabase = Room
            .databaseBuilder(context, GosdumaRoomDatabase::class.java, BuildConfig.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
        val databaseSource = GosdumaDatabaseSourceImpl(roomDatabase)
        val prefs = SettingsSharedPreferencesImpl(context)
        val resourceProvider = ResourceProviderImpl(context)
        maps[KeyValueStorage::class.java] = prefs
        maps[SettingsSharedPreferences::class.java] = prefs
        maps[ResourceProvider::class.java] = resourceProvider
        maps[NewsDistributor::class.java] = NewsDistributorImpl(context, resourceProvider)
        maps[SetupApiRepository::class.java] = SetupApiRepositoryImpl(prefs, BuildConfig.API_URL)
        maps[ArticlesApiRepository::class.java] = ArticlesApiRepositoryImpl(prefs, BuildConfig.API_URL)
        maps[ArticleCommentsApiRepository::class.java] = ArticleCommentsApiRepositoryImpl(prefs, BuildConfig.API_URL)
        maps[UsersApiRepository::class.java] = UsersApiRepositoryImpl(prefs, BuildConfig.API_URL)
        maps[DatabaseRepository::class.java] = DatabaseRepositoryImpl(databaseSource, prefs)
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
                getDatabaseRepository(),
                SourceNameMapper(getDatabaseRepository())
            )
            UsersInteractor::class.java -> maps[clazz] = UsersInteractorImpl(
                getUsersApiRepository(),
                getPreferences()
            )
            ArticleCommentsInteractor::class.java -> maps[clazz] = ArticleCommentsInteractorImpl(
                getArticlesApiRepository(),
                getArticleCommentsApiRepository(),
                getPreferences(),
                getDatabaseRepository(),
                SourceNameMapper(getDatabaseRepository())
            )
            SetupInteractor::class.java -> maps[clazz] = SetupInteractorImpl(
                getPreferences(),
                getSetupApiRepository(),
                getDatabaseRepository()
            )
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

    private fun getUsersApiRepository(): UsersApiRepository {
        return maps[UsersApiRepository::class.java] as UsersApiRepository
    }

    private fun getSetupApiRepository(): SetupApiRepository {
        return maps[SetupApiRepository::class.java] as SetupApiRepository
    }

    private fun getDatabaseRepository(): DatabaseRepository {
        return maps[DatabaseRepository::class.java] as DatabaseRepository
    }

    private fun getPreferences(): KeyValueStorage {
        return maps[KeyValueStorage::class.java] as KeyValueStorage
    }
}