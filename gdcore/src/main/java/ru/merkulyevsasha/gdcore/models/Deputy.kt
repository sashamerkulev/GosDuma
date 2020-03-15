package ru.merkulyevsasha.gdcore.models

data class Deputy(
    val id: Int,
    val name: String,
    val isCurrent: Boolean,
    val position: String,
    val avatarUrl: String,
    val authorization: String
)