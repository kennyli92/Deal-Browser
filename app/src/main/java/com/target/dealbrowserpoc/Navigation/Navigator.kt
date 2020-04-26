package com.target.dealbrowserpoc.Navigation

import androidx.appcompat.app.AppCompatActivity

interface Navigator {
    /**
     * Initializes the activity to use fragment manager
     */
    fun initializeContext(activity: AppCompatActivity)

    /**
     * releases the activity context to prevent memory leak
     */
    fun releaseContext()

    fun navigateDealsList()

    fun navigateDealsDetails(id: Int)
}
