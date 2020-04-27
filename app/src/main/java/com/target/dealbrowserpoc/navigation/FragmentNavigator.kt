package com.target.dealbrowserpoc.navigation

import androidx.appcompat.app.AppCompatActivity
import com.target.dealbrowserpoc.dealbrowser.R
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

  override fun navigateDealsList() {
    activity!!.supportFragmentManager
      .beginTransaction()
      .add(containerId, DealsListFragment())
      .commit()
  }

  override fun navigateDealsDetails(id: Int) {
    TODO("Not yet implemented")
  }
}