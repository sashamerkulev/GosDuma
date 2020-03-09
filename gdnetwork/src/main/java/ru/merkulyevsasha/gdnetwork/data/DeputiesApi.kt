package ru.merkulyevsasha.gdnetwork.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.merkulyevsasha.gdnetwork.models.DeputyPagesResponse

interface DeputiesApi {

    @GET("/deputies")
    suspend fun getDeputies(@Query("searchText") searchText: String,
                    @Query("page") page: Int,
                    @Query("pageSize") pageSize: Int,
                    @Query("orderFields") orderFields: String,
                    @Query("orderDirection") orderDirection: String
    ): Response<DeputyPagesResponse>

}
