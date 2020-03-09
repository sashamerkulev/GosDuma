package ru.merkulyevsasha.gdnetwork.models

import com.google.gson.annotations.SerializedName

data class DeputyPagesResponse(
    @field:SerializedName("Page") val page: Int,
    @field:SerializedName("PageSize") val pageSize: Int,
    @field:SerializedName("PageNumbers") val pageNumbers: Int,
    @field:SerializedName("RowNumbers") val rowNumbers: Int,
    @field:SerializedName("Deputies") val deputies: List<DeputyResponse>
)