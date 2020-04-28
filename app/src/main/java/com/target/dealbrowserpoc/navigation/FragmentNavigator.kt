package com.target.dealbrowserpoc.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.details.DealDetailsFragment
import com.target.dealbrowserpoc.deals.details.DealDetailsFragment.Companion.DEAL_ID_ARG
import com.target.dealbrowserpoc.deals.list.DealsListFragment

class FragmentNavigator : Navigator {
  private var activity: AppCompatActivity? = null
  private val containerId: Int = R.id.container

  override fun initializeContext(activity: AppCompatActivity) {
    if (this.activity == null) {
      this.activity = activity
    }
  }

  override fun releaseContext() {
    this.activity = null
  }

  override fun back() {
    val fm = this.activity!!.supportFragmentManager
    if (fm.backStackEntryCount > 0) {
      fm.popBackStack()
    } else {
      activity!!.onBackPressed()
    }
  }

  override fun navigateDealsList() {
    activity!!.supportFragmentManager
      .beginTransaction()
      .replace(containerId, DealsListFragment())
      .commit()
  }

  override fun navigateDealsDetails(id: String) {
    val fragment = DealDetailsFragment().also {
      val args = Bundle()
      args.putString(DEAL_ID_ARG, id)
      it.arguments = args
    }

    activity!!.supportFragmentManager
      .beginTransaction()
      .replace(containerId, fragment)
      .addToBackStack(null)
      .commit()
  }
}