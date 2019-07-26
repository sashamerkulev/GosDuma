package ru.merkulyevsasha.aktcomments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_comment.view.commentImageViewDislike
import kotlinx.android.synthetic.main.row_comment.view.commentImageViewLike
import kotlinx.android.synthetic.main.row_comment.view.commentTextViewDislike
import kotlinx.android.synthetic.main.row_comment.view.commentTextViewLike
import kotlinx.android.synthetic.main.row_comment.view.commenter
import kotlinx.android.synthetic.main.row_comment.view.imageViewCommentAvatar
import kotlinx.android.synthetic.main.row_comment.view.layoutCommentButtonDislike
import kotlinx.android.synthetic.main.row_comment.view.layoutCommentButtonLike
import kotlinx.android.synthetic.main.row_comment.view.layoutCommentButtonShare
import kotlinx.android.synthetic.main.row_comment.view.textViewComment
import kotlinx.android.synthetic.main.row_comment.view.textViewCommentDate
import kotlinx.android.synthetic.main.row_comment_article.view.imageViewDislike
import kotlinx.android.synthetic.main.row_comment_article.view.imageViewLike
import kotlinx.android.synthetic.main.row_comment_article.view.imageViewThumb
import kotlinx.android.synthetic.main.row_comment_article.view.layoutButtonDislike
import kotlinx.android.synthetic.main.row_comment_article.view.layoutButtonLike
import kotlinx.android.synthetic.main.row_comment_article.view.layoutButtonShare
import kotlinx.android.synthetic.main.row_comment_article.view.newsDateSource
import kotlinx.android.synthetic.main.row_comment_article.view.newsTitle
import kotlinx.android.synthetic.main.row_comment_article.view.textViewDislike
import kotlinx.android.synthetic.main.row_comment_article.view.textViewLike
import ru.merkulyevsasha.core.models.Article
import ru.merkulyevsasha.core.models.ArticleComment
import ru.merkulyevsasha.core.models.ArticleOrComment
import ru.merkulyevsasha.coreandroid.common.AvatarShower
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentShareCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktShareCallbackClickHandler
import java.text.SimpleDateFormat
import java.util.*

class AktCommentsViewAdapter constructor(
    private val context: Context,
    private val likeCallbackClickHandler: AktLikeCallbackClickHandler?,
    private val shareCallbackClickHandler: AktShareCallbackClickHandler?,
    private val commentLikeCallbackClickHandler: AktCommentLikeCallbackClickHandler?,
    private val commentShareCallbackClickHandler: AktCommentShareCallbackClickHandler?,
    private val colorThemeResolver: ColorThemeResolver,
    private val items: MutableList<ArticleOrComment>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ARTICLE_DETAIL_VIEW_TYPE = 1
        const val COMMENT_VIEW_TYPE = 2
    }

    private val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val avatarShower = AvatarShower()

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ARTICLE_DETAIL_VIEW_TYPE else COMMENT_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ARTICLE_DETAIL_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_comment_article, parent, false)
            return ArticleViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) bindArticleViewHolder(holder, position)
        else bindCommentViewHolder(holder as CommentViewHolder, position)
    }

    private fun bindCommentViewHolder(holder: CommentViewHolder, position: Int) {
        val item = items[position] as ArticleComment
        holder.itemView.commenter.text = item.userName
        holder.itemView.textViewComment.text = item.comment
        val pubDate = item.pubDate
        holder.itemView.textViewCommentDate.text = format.format(pubDate)

        avatarShower.showWithCache(context, R.drawable.ic_avatar_empty, item.avatarUrl, item.authorization, holder.itemView.imageViewCommentAvatar)

        holder.itemView.commentTextViewLike.text = item.usersLikeCount.toString()
        holder.itemView.commentTextViewDislike.text = item.usersDislikeCount.toString()

        colorThemeResolver.setArticleActivityColor(item.isUserLiked, holder.itemView.commentTextViewLike, holder.itemView.commentImageViewLike)
        colorThemeResolver.setArticleActivityColor(item.isUserDisliked,
            holder.itemView.commentTextViewDislike, holder.itemView.commentImageViewDislike)

        holder.itemView.layoutCommentButtonLike.setOnClickListener {
            commentLikeCallbackClickHandler?.onCommentLikeClicked(items[holder.adapterPosition] as AktComment)
        }

        holder.itemView.layoutCommentButtonDislike.setOnClickListener {
            commentLikeCallbackClickHandler?.onCommentDislikeClicked(items[holder.adapterPosition] as AktComment)
        }

        holder.itemView.layoutCommentButtonShare.setOnClickListener {
            commentShareCallbackClickHandler?.onCommentShareClicked(items[holder.adapterPosition] as AktComment)
        }

    }

    private fun bindArticleViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = items[position] as Article

        val source = item.sourceName
        val title = item.title.trim()
        val url = item.pictureUrl

        val pubDate = item.pubDate
        holder.itemView.newsDateSource.text = String.format("%s %s", format.format(pubDate), source)

        holder.itemView.imageViewThumb.setImageResource(0)
        if (url.isNullOrEmpty()) {
            holder.itemView.imageViewThumb.visibility = View.GONE
        } else {
            holder.itemView.imageViewThumb.visibility = View.VISIBLE
            Glide.with(context).load(url).into(holder.itemView.imageViewThumb)
        }

        holder.itemView.newsTitle.text = title

        holder.itemView.textViewLike.text = item.usersLikeCount.toString()
        holder.itemView.textViewDislike.text = item.usersDislikeCount.toString()
//            holder.itemView.textViewComment.text = item.usersCommentCount.toString()

        colorThemeResolver.setArticleActivityColor(item.isUserLiked, holder.itemView.textViewLike, holder.itemView.imageViewLike)
        colorThemeResolver.setArticleActivityColor(item.isUserDisliked, holder.itemView.textViewDislike, holder.itemView.imageViewDislike)
//            colorThemeResolver.setArticleActivityColor(item.isUserCommented, holder.itemView.textViewComment, holder.itemView.imageViewComment)

        holder.itemView.layoutButtonLike.setOnClickListener {
            likeCallbackClickHandler?.onArticleLikeClicked(items[holder.adapterPosition] as Akt)
        }

        holder.itemView.layoutButtonDislike.setOnClickListener {
            likeCallbackClickHandler?.onArticleDislikeClicked(items[holder.adapterPosition] as Akt)
        }

        holder.itemView.layoutButtonShare.setOnClickListener {
            shareCallbackClickHandler?.onArticleShareClicked(items[holder.adapterPosition] as Akt)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<ArticleOrComment>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun updateArticleItem(item: Akt) {
        this.items[0] = item
        notifyItemChanged(0)
    }

    fun updateCommentItem(item: AktComment) {
        for (index in 0 until items.size) {
            val itemm = items[index]
            if (itemm is ArticleComment && itemm.commentId == item.commentId) {
                this.items[index] = item
                notifyItemChanged(index)
                return
            }
        }
        this.items.add(item)
        notifyDataSetChanged()
    }

}

class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)