package ru.merkulyevsasha.gdnetwork.models

import com.google.gson.annotations.SerializedName

data class DeputyResponse(
    @field:SerializedName("Id") val id: Int,
    @field:SerializedName("Name") val name: String,
    @field:SerializedName("IsCurrent") val isCurrent: Boolean,
    @field:SerializedName("Position") val position: String
)