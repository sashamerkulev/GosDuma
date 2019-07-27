package ru.merkulyevsasha.aktdetails

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_aktdetails.imageViewComment
import kotlinx.android.synthetic.main.fragment_aktdetails.imageViewDislike
import kotlinx.android.synthetic.main.fragment_aktdetails.imageViewLike
import kotlinx.android.synthetic.main.fragment_aktdetails.layoutButtonComment
import kotlinx.android.synthetic.main.fragment_aktdetails.layoutButtonDislike
import kotlinx.android.synthetic.main.fragment_aktdetails.layoutButtonLike
import kotlinx.android.synthetic.main.fragment_aktdetails.layoutButtonShare
import kotlinx.android.synthetic.main.fragment_aktdetails.progressbar
import kotlinx.android.synthetic.main.fragment_aktdetails.textViewComment
import kotlinx.android.synthetic.main.fragment_aktdetails.textViewDislike
import kotlinx.android.synthetic.main.fragment_aktdetails.textViewLike
import kotlinx.android.synthetic.main.fragment_aktdetails.webview
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.RequireServiceLocator
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt

class AktDetailsFragment : Fragment(), AktDetailsView, RequireServiceLocator {
    companion object {
        private const val ARTICLE_ID = "ARTICLE_ID"
        @JvmStatic
        val TAG: String = AktDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(articleId: Int): Fragment {
            val fragment = AktDetailsFragment()
            val args = Bundle()
            args.putInt(ARTICLE_ID, articleId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var serviceLocator: ServiceLocator
    private var presenter: AktDetailsPresenterImpl? = null
    private lateinit var colorThemeResolver: ColorThemeResolver

    private var articleId = 0

    override fun setServiceLocator(serviceLocator: ServiceLocator) {
        this.serviceLocator = serviceLocator
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_aktdetails, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorThemeResolver = ColorThemeResolver(TypedValue(), requireContext().theme)

        layoutButtonLike.setOnClickListener {
            presenter?.onAktLikeClicked(articleId)
        }
        layoutButtonComment.setOnClickListener {
            presenter?.onAktCommentClicked(articleId)
        }
        layoutButtonDislike.setOnClickListener {
            presenter?.onAktDislikeClicked(articleId)
        }
        layoutButtonShare.setOnClickListener {
            presenter?.onAktShareClicked(articleId)
        }

        val bundle = savedInstanceState ?: arguments ?: return
        articleId = bundle.getInt(ARTICLE_ID, 0)
        val interactor = serviceLocator.get(AktsInteractor::class.java)
        presenter = AktDetailsPresenterImpl(interactor,
            serviceLocator.get(NewsDistributor::class.java), serviceLocator.get(MainActivityRouter::class.java))
        presenter?.bindView(this)
        presenter?.onFirstLoad(articleId)
    }

    override fun onPause() {
        presenter?.unbindView()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.bindView(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveFragmentState(outState)
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        presenter = null
        serviceLocator.release(ArticlesInteractor::class.java)
        super.onDestroy()
    }

    override fun showProgress() {
        progressbar?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressbar?.visibility = View.GONE
    }

    override fun showItem(item: Akt) {
        webview.webViewClient = ArticleDetailsViewClient()
        webview.loadUrl(item.link)

        updateItem(item)
    }

    override fun updateItem(item: Akt) {
        textViewLike.text = item.usersLikeCount.toString()
        textViewDislike.text = item.usersDislikeCount.toString()
        textViewComment.text = item.usersCommentCount.toString()

        colorThemeResolver.setArticleActivityColor(item.isUserLiked, textViewLike, imageViewLike)
        colorThemeResolver.setArticleActivityColor(item.isUserDisliked, textViewDislike, imageViewDislike)
        colorThemeResolver.setArticleActivityColor(item.isUserCommented, textViewComment, imageViewComment)
    }

    override fun showError() {
        Toast.makeText(requireContext(), getString(R.string.akt_details_loading_error_message), Toast.LENGTH_LONG).show()
    }

    private fun saveFragmentState(state: Bundle) {
        state.putInt(ARTICLE_ID, articleId)
    }

    private inner class ArticleDetailsViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideProgress()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showProgress()
            super.onPageStarted(view, url, favicon)
        }
    }

}