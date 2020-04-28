package com.target.dealbrowserpoc.deals.details

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.target.dealbrowserpoc.dealbrowser.R

val DealDetails = { func: DealDetailsRobot.() -> Unit ->
  DealDetailsRobot().apply { func() }
}

class DealDetailsRobot {
  fun isLandingScreen() {
    onView(withId(R.id.deal_details_container)).check(matches(isDisplayed()))
  }

  fun hasProductImage() {
    onView(withId(R.id.deal_details_image_view)).check(matches(isDisplayed()))
  }

  fun hasProductSalesPrice() {
    onView(withId(R.id.deal_details_sales_price)).check(matches(isDisplayed()))
  }

  fun hasProductRegularPrice() {
    onView(withId(R.id.deal_details_regular_price)).check(matches(isDisplayed()))
  }

  fun hasProductTitle() {
    onView(withId(R.id.deal_details_title)).check(matches(isDisplayed()))
  }

  fun hasAddToListButton() {
    onView(withId(R.id.deal_details_add_to_list_button)).check(matches(isDisplayed()))
  }

  fun hasAddToCartButton() {
    onView(withId(R.id.deal_details_add_to_cart_button)).check(matches(isDisplayed()))
  }
}