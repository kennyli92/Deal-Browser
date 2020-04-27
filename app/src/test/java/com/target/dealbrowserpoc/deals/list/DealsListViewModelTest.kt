package com.target.dealbrowserpoc.deals.list

import androidx.lifecycle.SavedStateHandle
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.deals.data.DealsResponse
import com.target.dealbrowserpoc.deals.recyclerView.DealsListItem
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class DealsListViewModelTest {
  @Before
  fun before() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.io() }
  }

  @After
  fun after() {
    RxAndroidPlugins.reset()
    RxJavaPlugins.reset()
  }

  private val dealsRepository: DealsRepository = mock()
  private val handle: SavedStateHandle = mock()

  private fun dealsListViewModel(): DealsListViewModel {
    return DealsListViewModel(
      handle = handle,
      dealsRepository = dealsRepository
    )
  }

  @Test
  fun on_load_action() {
    // GIVEN
    val dealsListViewModel = dealsListViewModel()
    val actionSignal = PublishSubject.create<DealsListAction>()
    dealsListViewModel.actionHandler(actionSignal = actionSignal)
    val deal = Deal(
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
    val deal2 = Deal(
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
    val dealList = listOf(deal, deal2)
    val dealsListItem = listOf(
      DealsListItem.DealView(deal = deal),
      DealsListItem.DealView(deal = deal2)
    )
    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(DealsResponse.Deals(deals = dealList) as DealsResponse)))

    // WHEN
    val stateObs = dealsListViewModel.stateObs().test()
    val eventObs = dealsListViewModel.eventObs().test()
    actionSignal.onNext(DealsListAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListState.ListItem(
        dealsListItem = dealsListItem
      ))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListEvent.Noop)
  }
}