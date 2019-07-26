package ru.merkulyevsasha.gdcoreandroid.common.aktadapter

import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktClickCallbackHandler {
    fun onArticleCliked(item: Akt)
}

interface AktLikeCallbackClickHandler {
    fun onArticleLikeClicked(item: Akt)
    fun onArticleDislikeClicked(item: Akt)
}

interface AktCommentLikeCallbackClickHandler {
    fun onCommentLikeClicked(item: AktComment)
    fun onCommentDislikeClicked(item: AktComment)
}

interface AktCommentCallbackClickHandler {
    fun onCommentArticleClicked(articleId: Int)
}

interface AktShareCallbackClickHandler {
    fun onArticleShareClicked(item: Akt)
}

interface AktCommentShareCallbackClickHandler {
    fun onCommentShareClicked(item: AktComment)
}