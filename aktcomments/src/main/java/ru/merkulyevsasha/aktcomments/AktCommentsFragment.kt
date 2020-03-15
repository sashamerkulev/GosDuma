package ru.merkulyevsasha.aktcomments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_aktcomments.adView
import kotlinx.android.synthetic.main.fragment_aktcomments.editTextComment
import kotlinx.android.synthetic.main.fragment_aktcomments.layoutAddCommentButton
import kotlinx.android.synthetic.main.fragment_aktcomments.recyclerView
import kotlinx.android.synthetic.main.fragment_aktcomments.swipeRefreshLayout
import ru.merkulyevsasha.core.RequireServiceLocator
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.core.domain.ArticleCommentsInteractor
import ru.merkulyevsasha.core.models.ArticleOrComment
import ru.merkulyevsasha.coreandroid.common.AdViewHelper
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment
import java.util.*

class AktCommentsFragment : Fragment(), AktCommentsView, RequireServiceLocator {

    companion object {
        private const val KEY_POSITION = "key_position"
        private const val ARTICLE_ID = "ARTICLE_ID"
        @JvmStatic
        val TAG: String = "AktCommentsFragment"

        @JvmStatic
        fun newInstance(articleId: Int): Fragment {
            val fragment = AktCommentsFragment()
            val args = Bundle()
            args.putInt(ARTICLE_ID, articleId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var serviceLocator: ServiceLocator
    private var presenter: AktCommentsPresenterImpl? = null
    private lateinit var colorThemeResolver: ColorThemeResolver

    private lateinit var adapter: AktCommentsViewAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var articleId = 0
    private var position = 0

    override fun setServiceLocator(serviceLocator: ServiceLocator) {
        this.serviceLocator = serviceLocator
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_aktcomments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorThemeResolver = ColorThemeResolver(TypedValue(), requireContext().theme)

        val bundle = savedInstanceState ?: arguments ?: return
        articleId = bundle.getInt(ARTICLE_ID, 0)
        position = bundle.getInt(KEY_POSITION, 0)

        AdViewHelper.loadBannerAd(adView, BuildConfig.DEBUG_MODE)

        presenter = serviceLocator.get(AktCommentsPresenterImpl::class.java)
        presenter?.bindView(this)

        initRecyclerView()

        presenter?.onFirstLoad(articleId)

        layoutAddCommentButton.setOnClickListener {
            presenter?.onAddCommentClicked(articleId, editTextComment.text.toString())
        }

        colorThemeResolver.initSwipeRefreshColorScheme(swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            presenter?.onRefresh(articleId)
        }
    }

    override fun onPause() {
        adView?.pause()
        presenter?.unbindView()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView?.resume()
        presenter?.bindView(this)
    }

    override fun onDestroy() {
        adView?.destroy()
        presenter?.onDestroy()
        presenter = null
        serviceLocator.release(ArticleCommentsInteractor::class.java)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveFragmentState(outState)
    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showError() {
        Toast.makeText(requireContext(), getString(R.string.aktcomment_loading_error_message), Toast.LENGTH_LONG).show()
    }

    override fun showComments(items: List<ArticleOrComment>) {
        adapter.setItems(items)
    }

    override fun updateItem(item: Akt) {
        adapter.updateArticleItem(item)
    }

    override fun updateCommentItem(item: AktComment) {
        editTextComment.setText("")
        ru.merkulyevsasha.coreandroid.common.KbUtils.hideKeyboard(requireActivity())
        adapter.updateCommentItem(item)
    }

    private fun initSwipeRefreshColorScheme() {
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(colorThemeResolver.getThemeAttrColor(R.attr.colorAccent))
        swipeRefreshLayout.setColorSchemeColors(colorThemeResolver.getThemeAttrColor(R.attr.colorControlNormal))
    }

    private fun saveFragmentState(state: Bundle) {
        position = layoutManager.findFirstVisibleItemPosition()
        state.putInt(KEY_POSITION, position)
        state.putInt(ARTICLE_ID, articleId)
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AktCommentsViewAdapter(
            requireContext(),
            presenter,
            presenter,
            presenter,
            presenter,
            colorThemeResolver,
            ArrayList()
        )
        recyclerView.adapter = adapter
    }
}