package ru.merkulyevsasha.deputyrequestdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.merkulyevsasha.gdcore.GDServiceLocator
import ru.merkulyevsasha.gdcore.RequireGDServiceLocator

class DeputyRequestDetailsFragment : Fragment(), RequireGDServiceLocator {

    companion object {
        @JvmStatic
        val TAG: String = DeputyRequestDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            val fragment = DeputyRequestDetailsFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    override fun setGDServiceLocator(serviceLocator: GDServiceLocator) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_deputyrequest_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
//        adView?.pause()
//        presenter?.unbindView()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
//        adView?.resume()
//        presenter?.bindView(this)
    }

    override fun onDestroyView() {
//        adView?.destroy()
//        presenter?.onDestroy()
        super.onDestroyView()
    }

}
