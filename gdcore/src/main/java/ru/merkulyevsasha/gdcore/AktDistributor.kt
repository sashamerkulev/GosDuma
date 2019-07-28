package ru.merkulyevsasha.gdcore

import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktDistributor {
    fun distribute(item: Akt)
    fun distribute(item: AktComment)
}