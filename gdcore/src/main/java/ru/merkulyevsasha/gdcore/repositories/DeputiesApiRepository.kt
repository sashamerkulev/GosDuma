package ru.merkulyevsasha.gdcore.repositories

import ru.merkulyevsasha.gdcore.models.DeputyPages

interface DeputiesApiRepository {
    suspend fun getDeputies(searchText: String, page: Int, pageSize: Int,
                    orderFields: String, orderDirection: String): DeputyPages
}