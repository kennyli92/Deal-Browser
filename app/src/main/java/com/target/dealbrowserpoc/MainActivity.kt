package com.target.dealbrowserpoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.navigation.FragmentNavigator
import com.target.dealbrowserpoc.navigation.Navigator
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
  Navigator by FragmentNavigator() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    this.initializeContext(activity = this)
    if (savedInstanceState == null) {
      navigateDealsList()
    }
  }

  override fun onStart() {
    super.onStart()
    initializeContext(activity = this)
  }

  override fun onStop() {
    super.onStop()
    releaseContext()
  }
}