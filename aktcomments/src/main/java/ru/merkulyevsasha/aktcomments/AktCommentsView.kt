package ru.merkulyevsasha.aktcomments

import ru.merkulyevsasha.core.models.ArticleOrComment
import ru.merkulyevsasha.coreandroid.base.BaseView
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment

interface AktCommentsView : BaseView {
    fun showProgress()
    fun hideProgress()
    fun showError()
    fun showComments(items: List<ArticleOrComment>)
    fun updateItem(item: Akt)
    fun updateCommentItem(item: AktComment)
}
