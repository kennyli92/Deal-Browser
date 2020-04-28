package com.target.dealbrowserpoc.deals.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.target.dealbrowserpoc.dealbrowser.R

val DealsList = { func: DealsListRobot.() -> Unit ->
  DealsListRobot().apply { func() }
}

class DealsListRobot {
  fun isLandingScreen() {
    onView(withId(R.id.deals_list_container)).check(matches(isDisplayed()))
    onView(withId(R.id.deals_list_recycler_view)).check(matches(isDisplayed()))
  }
}