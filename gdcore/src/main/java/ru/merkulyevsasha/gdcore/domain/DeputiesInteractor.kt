package ru.merkulyevsasha.gdcore.domain

import ru.merkulyevsasha.gdcore.models.DeputyPages

interface DeputiesInteractor {
    suspend fun getDeputies(searchText: String, orderDirection: String): DeputyPages
}