package ru.merkulyevsasha.gosduma.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.bottomNav
import ru.merkulyevsasha.core.RequireServiceLocator
import ru.merkulyevsasha.core.ServiceLocator
import ru.merkulyevsasha.gdcore.routers.GDMainFragmentRouter
import ru.merkulyevsasha.gosduma.R

class MainFragment : Fragment(), RequireServiceLocator {

    companion object {
        @JvmStatic
        val TAG: String = MainFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = MainFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    private lateinit var mainFragmentRouter: GDMainFragmentRouter

    private val navigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_articles -> {
                    mainFragmentRouter.showArticles()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_deputies -> {
                    mainFragmentRouter.showDeputies()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_laws -> {
                    mainFragmentRouter.showLaws()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_user -> {
                    mainFragmentRouter.showUserInfo()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_akt -> {
                    mainFragmentRouter.showAkts()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun setServiceLocator(serviceLocator: ServiceLocator) {
        mainFragmentRouter = serviceLocator.get(GDMainFragmentRouter::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        if (savedInstanceState == null) {
            mainFragmentRouter.showArticles()
        }
    }
}