package com.target.dealbrowserpoc.dealbrowser

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.target.dealbrowserpoc.navigation.FragmentNavigator
import com.target.dealbrowserpoc.navigation.Navigator
import com.target.dealbrowserpoc.dealbrowser.DealListFragment.OnFragmentInteractionListener
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
        OnFragmentInteractionListener,
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

    override fun onDestroy() {
        super.onDestroy()

        this.releaseContext()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Don't create the menu for now
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(id: String?) {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Product Id")
        alertDialog.setMessage(id)
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}
