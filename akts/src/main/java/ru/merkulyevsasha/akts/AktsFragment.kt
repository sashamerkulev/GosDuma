package ru.merkulyevsasha.akts

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_akts.adView
import kotlinx.android.synthetic.main.fragment_akts.buttonUp
import kotlinx.android.synthetic.main.fragment_akts.recyclerView
import kotlinx.android.synthetic.main.fragment_akts.swipeRefreshLayout
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.RequireServiceLocator
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.core.domain.ArticlesInteractor
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.coreandroid.common.AdViewHelper
import ru.merkulyevsasha.coreandroid.common.AppbarScrollExpander
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.coreandroid.common.ShowActionBarListener
import ru.merkulyevsasha.coreandroid.common.ToolbarCombinator
import ru.merkulyevsasha.gdcore.domain.AktInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktViewAdapter

class AktsFragment : Fragment(), AktsView, RequireServiceLocator {

    companion object {

        private const val MAX_POSITION = 5
        private const val KEY_POSITION = "key_position"
        private const val KEY_EXPANDED = "key_expanded"
        private const val KEY_SEARCH_TEXT = "key_search_text"

        @JvmStatic
        val TAG: String = AktsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = AktsFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appbarLayout: AppBarLayout

    private lateinit var serviceLocator: ServiceLocator
    private var presenter: AktsPresenterImpl? = null
    private var combinator: ToolbarCombinator? = null

    private lateinit var adapter: AktViewAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var colorThemeResolver: ColorThemeResolver

    private lateinit var appbarScrollExpander: AppbarScrollExpander
    private var expanded = true
    private var position = 0

    private lateinit var searchItem: MenuItem
    private lateinit var searchView: SearchView
    private var searchText: String? = null

    override fun setServiceLocator(serviceLocator: ServiceLocator) {
        this.serviceLocator = serviceLocator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ToolbarCombinator) {
            combinator = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_akts, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedState = savedInstanceState ?: arguments
        savedState?.apply {
            position = this.getInt(KEY_POSITION, 0)
            expanded = this.getBoolean(KEY_EXPANDED, true)
            searchText = this.getString(KEY_SEARCH_TEXT, searchText)
        }

        colorThemeResolver = ColorThemeResolver(TypedValue(), requireContext().theme)

        toolbar = view.findViewById(R.id.toolbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsinngToolbarLayout)
        appbarLayout = view.findViewById(R.id.appbarLayout)

        toolbar.setTitle(R.string.fragment_akt_title)
        toolbar.setTitleTextColor(colorThemeResolver.getThemeAttrColor(R.attr.actionBarTextColor))
        collapsingToolbarLayout.isTitleEnabled = false
        combinator?.bindToolbar(toolbar)

        appbarScrollExpander = AppbarScrollExpander(recyclerView, object : ShowActionBarListener {
            override fun onShowActionBar(show: Boolean) {
                appbarLayout.setExpanded(show)
            }
        })

        swipeRefreshLayout.setOnRefreshListener { presenter?.onRefresh() }
        colorThemeResolver.initSwipeRefreshColorScheme(swipeRefreshLayout)

        AdViewHelper.loadBannerAd(adView, BuildConfig.DEBUG_MODE)

        val interactor = serviceLocator.get(AktInteractor::class.java)
        presenter = AktsPresenterImpl(interactor, serviceLocator.get(NewsDistributor::class.java),
            serviceLocator.get(MainActivityRouter::class.java))
        presenter?.bindView(this)

        initRecyclerView()
        initBottomUp()

        if (searchText.isNullOrEmpty()) {
            presenter?.onFirstLoad()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.articles_menu, menu)
        menu?.let {
            searchItem = it.findItem(R.id.action_search)
            searchView = searchItem.actionView as SearchView
            val searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchEditText.setTextColor(colorThemeResolver.getThemeAttrColor(R.attr.colorControlNormal))
            if (!searchText.isNullOrEmpty()) {
                searchItem.expandActionView()
                searchView.setQuery(searchText, false)
                searchView.clearFocus()
                presenter?.onSearch(searchText)
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchText = query
                    presenter?.onSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        searchText = ""
                        presenter?.onSearch(newText)
                    }
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_refresh) {
            presenter?.onRefresh()
            return true
        } else if (item?.itemId == R.id.action_search) {
            return true
        }
        return super.onOptionsItemSelected(item)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveFragmentState(outState)
    }

    override fun onDestroyView() {
        adView?.destroy()
        combinator?.unbindToolbar()
        presenter?.onDestroy()
        presenter = null
        serviceLocator.release(ArticlesInteractor::class.java)
        saveFragmentState(arguments ?: Bundle())
        super.onDestroyView()
    }

    override fun showError() {
        Toast.makeText(requireContext(), getString(R.string.akt_loading_error_message), Toast.LENGTH_LONG).show()
    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }

    override fun showItems(items: List<Akt>) {
        adapter.setItems(items)
        layoutManager.scrollToPosition(position)
    }

    override fun updateItems(items: List<Akt>) {
        adapter.updateItems(items)
    }

    override fun updateItem(item: Akt) {
        adapter.updateItem(item)
    }

    private fun initBottomUp() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (position >= layoutManager.findFirstVisibleItemPosition()) {
                    position = layoutManager.findFirstVisibleItemPosition()
                    if (position > MAX_POSITION && !swipeRefreshLayout.isRefreshing) {
                        buttonUp.visibility = View.VISIBLE
                    } else {
                        buttonUp.visibility = View.GONE
                    }
                } else {
                    position = layoutManager.findFirstVisibleItemPosition() - 1
                    buttonUp.visibility = View.GONE
                }
            }
        })
        buttonUp.setOnClickListener {
            layoutManager.scrollToPosition(0)
            position = 0
            buttonUp.visibility = View.GONE
            appbarLayout.setExpanded(true)
        }
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = AktViewAdapter(
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

    private fun saveFragmentState(state: Bundle) {
        position = layoutManager.findFirstVisibleItemPosition()
        state.putInt(KEY_POSITION, position)
        state.putBoolean(KEY_EXPANDED, expanded)
        state.putString(KEY_SEARCH_TEXT, searchText)
    }
}