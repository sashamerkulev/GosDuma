package ru.merkulyevsasha.gdcore.models

data class DeputyPages(
    val page: Int,
    val pageSize: Int,
    val pageNumbers: Int,
    val rowNumbers: Int,
    val deputies: List<Deputy>
)