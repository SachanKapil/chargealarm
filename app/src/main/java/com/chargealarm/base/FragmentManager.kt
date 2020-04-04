package com.chargealarm.base

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class FragmentManager(
    private var container: ViewGroup,
    private var mFragmentManager: FragmentManager
) {

    private var activeFragment: BaseFragment? = null

    abstract fun getItem(position: Int): BaseFragment

    fun changeFragment(position: Int, tag: String): BaseFragment? {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        var fragment = mFragmentManager.findFragmentByTag(tag)
        fragment?.let {
            fragmentTransaction.show(it)
        } ?: run {
            fragment = getItem(position)
            fragmentTransaction.add(container.id, fragment as BaseFragment, tag)
        }
        if (activeFragment != null && activeFragment !== fragment) {
            fragmentTransaction.hide(activeFragment as Fragment)
        }
        activeFragment = fragment as BaseFragment
        // Set fragment as primary navigator for child manager back stack to be handled by system
        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
        return fragment as BaseFragment
    }

    /**
     * Removes Fragment from Fragment Manager and clears all saved states. Call to changeFragment()
     * will restart fragment from fresh state.
     *
     * @param position
     */
    fun removeFragment(position: Int, tag: String) {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        mFragmentManager.findFragmentByTag(tag)?.let {
            fragmentTransaction.remove(it)
        }
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    fun getCurrentVisibleFragment(): Fragment? {
        var currentVisibleFragment: Fragment? = null
        val fragments = mFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible) {
                currentVisibleFragment = fragment
            }
        }
        return currentVisibleFragment
    }
}