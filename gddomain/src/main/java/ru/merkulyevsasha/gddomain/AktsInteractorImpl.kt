package ru.merkulyevsasha.gddomain

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import ru.merkulyevsasha.gdcore.repositories.AktsApiRepository
import ru.merkulyevsasha.gddomain.mappers.AktSourceNameMapper
import java.util.*

class AktsInteractorImpl(
    private val aktsApiRepository: AktsApiRepository,
    private val keyValueStorage: SettingsSharedPreferences,
    private val databaseRepository: GDDatabaseRepository,
    private val sourceNameMapper: AktSourceNameMapper
) : AktsInteractor {

    companion object {
        private const val NOT_USER_ACTIVITIES_HOURS = 24 * 7 // week
        private const val USER_ACTIVITIES_HOURS = 24 * 30 // month
    }

    override fun getAkts(): Single<List<Akt>> {
        return Single.fromCallable {
//            val cleanDate = Calendar.getInstance()
//            cleanDate.add(Calendar.HOUR, -NOT_USER_ACTIVITIES_HOURS)
//            databaseRepository.removeOldNotUserActivityArticles(cleanDate.time)
//            cleanDate.add(Calendar.HOUR, -USER_ACTIVITIES_HOURS)
//            databaseRepository.removeOldUserActivityArticles(cleanDate.time)
        }
            .flatMap {
                databaseRepository.getAkts()
                    .flatMap { items ->
                        if (items.isEmpty()) refreshAndGetAkts()
                        else Single.just(items.map { sourceNameMapper.map(it) })
                    }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun refreshAndGetAkts(): Single<List<Akt>> {
        return Single.fromCallable { keyValueStorage.getLastArticleReadDate() ?: Date(0) }
            .flatMap {
                aktsApiRepository.getAkts(it)
                    .doOnSuccess { items ->
                        if (items.isNotEmpty()) {
                            databaseRepository.addOrUpdateAkts(items)
                            keyValueStorage.setLastArticleReadDate(Date())
                        }
                    }
            }
            .flattenAsFlowable { it }
            .map { sourceNameMapper.map(it) }
            .toList()
            .subscribeOn(Schedulers.io())
    }

    override fun searchAkts(searchText: String?, byUserActivities: Boolean): Single<List<Akt>> {
        return Single.fromCallable { searchText ?: "" }
            .flatMap { st: String ->
                if (st.isEmpty()) getAkts()
                else
                    databaseRepository.searchAkts(st)
                        .flattenAsFlowable { it }
                        .map { sourceNameMapper.map(it) }
                        .toList()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun likeAkt(articleId: Int): Single<Akt> {
        return aktsApiRepository.likeAkt(articleId)
            .doOnSuccess {
                databaseRepository.updateAkt(it)
            }
            .map { sourceNameMapper.map(it) }
            .subscribeOn(Schedulers.io())
    }

    override fun dislikeAkt(articleId: Int): Single<Akt> {
        return aktsApiRepository.dislikeAkt(articleId)
            .doOnSuccess {
                databaseRepository.updateAkt(it)
            }
            .map { sourceNameMapper.map(it) }
            .subscribeOn(Schedulers.io())
    }

    override fun getAkt(articleId: Int): Single<Akt> {
        return aktsApiRepository.getAkt(articleId)
            .doOnSuccess {
                databaseRepository.updateAkt(it)
            }
            .map { sourceNameMapper.map(it) }
            .subscribeOn(Schedulers.io())
    }
}
