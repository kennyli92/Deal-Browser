package com.target.dealbrowserpoc.deals.list

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.target.dealbrowserpoc.MainActivity
import com.target.dealbrowserpoc.MockApp
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.deals.data.Deals
import com.target.dealbrowserpoc.stub.DealsState
import com.target.dealbrowserpoc.stub.MockStubber
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class DealsListFragmentTest {
  @get:Rule
  val mainActivityTestRule = IntentsTestRule(MainActivity::class.java, false, false)

  private lateinit var mockStubber: MockStubber

  @Before
  fun setUp() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

    val mockApp: MockApp = ApplicationProvider.getApplicationContext()
    mockStubber = MockStubber(injector = mockApp)
  }

  @After
  fun cleanUp() {
    RxAndroidPlugins.reset()
    RxJavaPlugins.reset()
  }

  val state = { func: MockStubber.() -> Unit ->
    mockStubber.apply { func() }
  }

  private val deal = Deal(
    id = "1",
    aisle = "A1",
    description = "deal 1",
    guid = "guid 1",
    image = "image1Url",
    index = 0,
    price = "$1.23",
    salePrice = null,
    title = "Deal 1"
  )
  private val deal2 = Deal(
    id = "2",
    aisle = "A2",
    description = "deal 2",
    guid = "guid 2",
    image = "image2Url",
    index = 0,
    price = "$456.78",
    salePrice = "$4.56",
    title = "Deal 2"
  )
  private val dealList = listOf(deal, deal2)
  private val deals = Deals(
    id = "id",
    type = "list",
    data = dealList
  )

  @Test
  fun display_deals_list_when_get_deals_api_returns_data_and_navigate_to_deal_details() {
    state {
      dealsState(dealsState = DealsState.GetDeals(
        response = DealsState.GetDealsResponse.Success(
          deals = deals
        )
      ))
    }

    mainActivityTestRule.launchActivity(null)

    DealsList {
      isLandingScreen()
      hasDealAtPosition(position = 0)
      hasDealAtPosition(position = 1)

      tapDealAtPosition(position = 0)
    }.navigateToDealDetails {
      isLandingScreen()

      hasProductImage()
      hasProductSalesPrice()
      hasProductRegularPrice()
      hasProductTitle()
      hasAddToListButton()
      hasAddToCartButton()
    }
  }
}