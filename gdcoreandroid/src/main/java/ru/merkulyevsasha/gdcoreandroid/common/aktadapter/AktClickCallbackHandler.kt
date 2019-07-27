package ru.merkulyevsasha.gdcoreandroid.common.aktadapter

import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktClickCallbackHandler {
    fun onAktCliked(item: Akt)
}

interface AktLikeCallbackClickHandler {
    fun onAktLikeClicked(item: Akt)
    fun onAktDislikeClicked(item: Akt)
}

interface AktCommentLikeCallbackClickHandler {
    fun onAktCommentLikeClicked(item: AktComment)
    fun onAktCommentDislikeClicked(item: AktComment)
}

interface AktCommentCallbackClickHandler {
    fun onAktCommentClicked(articleId: Int)
}

interface AktShareCallbackClickHandler {
    fun onAktShareClicked(item: Akt)
}

interface AktCommentShareCallbackClickHandler {
    fun onAktCommentShareClicked(item: AktComment)
}