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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_deputies.adView
import kotlinx.android.synthetic.main.fragment_deputies.recyclerView
import kotlinx.android.synthetic.main.fragment_deputies.swipeRefreshLayout
import ru.merkulyevsasha.coreandroid.common.AdViewHelper
import ru.merkulyevsasha.coreandroid.common.AppbarScrollExpander
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.coreandroid.common.ShowActionBarListener
import ru.merkulyevsasha.coreandroid.common.ToolbarCombinator

class DeputiesFragment : Fragment(), DeputiesView {

    companion object {
        @JvmStatic
        val TAG: String = DeputiesFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = DeputiesFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private lateinit var toolbar: Toolbar
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appbarLayout: AppBarLayout
    private lateinit var colorThemeResolver: ColorThemeResolver
    private lateinit var appbarScrollExpander: AppbarScrollExpander

    private var combinator: ToolbarCombinator? = null
    private var presenter: DeputiesPresenter? = null

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

        swipeRefreshLayout.setOnRefreshListener { }
        colorThemeResolver.initSwipeRefreshColorScheme(swipeRefreshLayout)

        AdViewHelper.loadBannerAd(adView)
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

    override fun onDestroyView() {
        adView?.destroy()
        presenter?.onDestroy()
        super.onDestroyView()
    }

}
