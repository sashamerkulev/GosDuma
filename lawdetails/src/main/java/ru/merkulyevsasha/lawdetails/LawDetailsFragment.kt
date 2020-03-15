package ru.merkulyevsasha.lawdetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import ru.merkulyevsasha.coreandroid.common.AppbarScrollExpander
import ru.merkulyevsasha.coreandroid.common.ColorThemeResolver
import ru.merkulyevsasha.coreandroid.common.ToolbarCombinator
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.RequireGDServiceLocator

class LawDetailsFragment : Fragment(), LawDetailsView, RequireGDServiceLocator {

    companion object {
        @JvmStatic
        val TAG: String = "LawDetailsFragment"

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = LawDetailsFragment()
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
    private var presenter: LawDetailsPresenter? = null

    override fun setGDServiceLocator(serviceLocator: GDServiceLocator) {
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
        inflater.inflate(R.layout.fragment_law_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
//        adView?.pause()
        presenter?.unbindView()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
//        adView?.resume()
        presenter?.bindView(this)
    }

    override fun onDestroyView() {
//        adView?.destroy()
        presenter?.onDestroy()
        super.onDestroyView()
    }

}
