package ru.merkulyevsasha.deputies

import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor
import ru.merkulyevsasha.gdcore.models.DeputyPages
import ru.merkulyevsasha.gdcore.repositories.DeputiesApiRepository

class DeputiesInteractorImpl(private val repo: DeputiesApiRepository) : DeputiesInteractor {

    override suspend fun getDeputies(searchText: String, orderDirection: String): DeputyPages {
        return repo.getDeputies(searchText, 1, 500, "", orderDirection)
    }

}