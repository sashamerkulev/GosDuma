package ru.merkulyevsasha.deputies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_deputies.adView
import kotlinx.android.synthetic.main.fragment_deputies.emptyText
import kotlinx.android.synthetic.main.fragment_deputies.recyclerView
import kotlinx.android.synthetic.main.fragment_deputies.swipeRefreshLayout
import kotlinx.android.synthetic.main.row_deputy.view.current
import kotlinx.android.synthetic.main.row_deputy.view.imageViewAvatar
import kotlinx.android.synthetic.main.row_deputy.view.name
import kotlinx.android.synthetic.main.row_deputy.view.position
import ru.merkulyevsasha.coreandroid.common.AdViewHelper
import ru.merkulyevsasha.coreandroid.common.AppbarScrollExpander
import ru.merkulyevsasha.coreandroid.common.AvatarShower
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.coreandroid.common.ShowActionBarListener
import ru.merkulyevsasha.coreandroid.common.ToolbarCombinator
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.RequireGDServiceLocator
import ru.merkulyevsasha.gdcore.models.Deputy

class DeputiesFragment : Fragment(), DeputiesView, RequireGDServiceLocator {

    companion object {
        private const val MAX_POSITION = 5
        private const val KEY_POSITION = "key_position"
        private const val KEY_EXPANDED = "key_expanded"
        private const val KEY_SEARCH_TEXT = "key_search_text"

        @JvmStatic
        val TAG: String = "DeputiesFragment"

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = DeputiesFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private lateinit var serviceLocator: GDServiceLocator

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appbarLayout: AppBarLayout
    private lateinit var colorThemeResolver: ColorThemeResolver
    private lateinit var appbarScrollExpander: AppbarScrollExpander

    private lateinit var adapter: DeputiesAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var combinator: ToolbarCombinator? = null
    private var presenter: DeputiesPresenterImpl? = null

    private var expanded = true
    private var position = 0
    private var searchText: String? = null

    override fun setGDServiceLocator(serviceLocator: GDServiceLocator) {
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
        inflater.inflate(R.layout.fragment_deputies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savedState = savedInstanceState ?: arguments
        savedState?.apply {
            position = this.getInt(KEY_POSITION, 0)
            expanded = this.getBoolean(KEY_EXPANDED, true)
            searchText = this.getString(KEY_SEARCH_TEXT, searchText)
        }

        colorThemeResolver = ColorThemeResolver(android.util.TypedValue(), requireContext().theme)

        toolbar = view.findViewById(R.id.toolbar)
        collapsingToolbarLayout = view.findViewById(R.id.collapsinngToolbarLayout)
        appbarLayout = view.findViewById(R.id.appbarLayout)

        toolbar.setTitle(R.string.bottom_button_deputies_title)
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

        presenter = serviceLocator.get(DeputiesPresenterImpl::class.java)
        presenter?.bindView(this)
        presenter?.onFirstLoad()

        initRecyclerView()

        if (searchText.isNullOrEmpty()) {
            presenter?.onFirstLoad()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.deputies_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_filter) {
            return true
        } else if (item?.itemId == R.id.action_sort) {
            return true
        } else return super.onOptionsItemSelected(item)
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
        saveFragmentState(arguments ?: Bundle())
        adView?.destroy()
        presenter?.onDestroy()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }

    override fun showItems(items: List<Deputy>) {
        if (items.isEmpty()) {
            emptyText?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
        } else {
            emptyText?.visibility = View.GONE
            recyclerView?.visibility = View.VISIBLE
            adapter.setItems(items)
            layoutManager.scrollToPosition(position)
        }
    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = DeputiesAdapter(
            requireContext(),
            colorThemeResolver,
            presenter!!,
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

    class DeputiesAdapter(
        private val context: Context,
        private val colorThemeResolver: ColorThemeResolver,
        private val onDeputyClickListener: OnDeputyClickListener,
        private val items: MutableList<Deputy>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val avatarShower = AvatarShower()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_deputy, parent, false)
            return DeputyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as DeputyViewHolder).bind(position)
        }

        fun setItems(items: List<Deputy>) {
            this.items.clear()
            this.items.addAll(items)
            this.notifyDataSetChanged()
        }

        inner class DeputyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(position: Int) {
                val item = items[position]
                itemView.name.text = item.name
                itemView.position.text = item.position
                itemView.current.setText(if (item.isCurrent) R.string.deputy_current else R.string.deputy_not_current)
                avatarShower.showWithoutCache(context, R.drawable.ic_avatar_empty, item.avatarUrl,
                    item.authorization, itemView.imageViewAvatar)
                itemView.setOnClickListener { onDeputyClickListener.onDeputyClicked(item.id) }
            }
        }
    }

}
