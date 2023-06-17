package com.backstackfragment.BackStack

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.tarkovinfo.Tools.BackStackKT.BackStackEntry
import com.tarkovinfo.Tools.BackStackKT.BackStackManager


abstract class BackStackActivity(private val containerViewId: Int) : AppCompatActivity() {

    private var backStackManager: BackStackManager? = null
    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        backStackManager = BackStackManager()
    }

    fun showFragment(fragment: Fragment): Fragment {
        showFragmentRP(fragment, true)
        return fragment
    }

    private fun showFragmentRP(fragment: Fragment, addToBackStack: Boolean) {
        if (curFragment != null && addToBackStack) {
            pushFragmentToBackStack(curTabId, curFragment!!)
        }
        replaceFragment(fragment)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val tr = fragmentManager.beginTransaction()
        tr.replace(containerViewId, fragment)
        tr.commitAllowingStateLoss()
        curFragment = fragment
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val pair = popFragmentFromBackStack()
        if (pair != null) {
            backTo(pair.first!!, pair.second!!)
        } else {
            super.onBackPressed()
        }
    }

    private fun backTo(tabId: Int, fragment: Fragment) {
        if (tabId != curTabId) {
            curTabId = tabId
        }
        replaceFragment(fragment)
        supportFragmentManager.executePendingTransactions()
    }

    override fun onDestroy() {
        backStackManager = null
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_BACK_STACK_MANAGER, backStackManager!!.saveState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        backStackManager!!.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK_MANAGER))
    }

    private fun pushFragmentToBackStack(hostId: Int, fragment: Fragment) {
        try {
            val entry = BackStackEntry.create(
                supportFragmentManager, fragment
            )
            backStackManager!!.push(hostId, entry)
        } catch (e: Exception) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e)
        }
    }

    private fun popFragmentFromBackStack(): Pair<Int, Fragment>? {
        val pair = backStackManager!!.pop()
        return if (pair != null) Pair.create(pair.first, pair.second.toFragment(this)) else null
    }

    companion object {

        private const val STATE_BACK_STACK_MANAGER = "back_stack_manager"

        @JvmField
        var curTabId = 0

        @JvmField
        var curFragment: Fragment? = null
    }
}