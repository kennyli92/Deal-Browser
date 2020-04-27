package com.target.dealbrowserpoc.deals.list

import androidx.lifecycle.SavedStateHandle
import com.google.android.material.snackbar.Snackbar
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.deals.data.DealsResponse
import com.target.dealbrowserpoc.deals.recyclerView.DealsListItem
import com.target.dealbrowserpoc.dialog.SnackbarViewModel
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.RuntimeException
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

  private fun dealsListViewModel(): DealsListViewModel {
    return DealsListViewModel(
      handle = handle,
      dealsRepository = dealsRepository
    )
  }

  @Test
  fun on_load_action_when_get_deals_returns_data() {
    // GIVEN
    val dealsListViewModel = dealsListViewModel()
    val actionSignal = PublishSubject.create<DealsListAction>()
    dealsListViewModel.actionHandler(actionSignal = actionSignal)

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

  @Test
  fun on_load_action_when_get_deals_returns_not_found() {
    // GIVEN
    val dealsListViewModel = dealsListViewModel()
    val actionSignal = PublishSubject.create<DealsListAction>()
    dealsListViewModel.actionHandler(actionSignal = actionSignal)

    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(DealsResponse.NotFound as DealsResponse)))

    // WHEN
    val stateObs = dealsListViewModel.stateObs().test()
    val eventObs = dealsListViewModel.eventObs().test()
    actionSignal.onNext(DealsListAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListEvent.Snackbar(
        vm = SnackbarViewModel(
          messageResId = R.string.deals_get_not_found,
          duration = Snackbar.LENGTH_LONG
        )
      ))
  }

  @Test
  fun on_load_action_when_get_deals_returns_unknown_error() {
    // GIVEN
    val dealsListViewModel = dealsListViewModel()
    val actionSignal = PublishSubject.create<DealsListAction>()
    dealsListViewModel.actionHandler(actionSignal = actionSignal)

    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(
        DealsResponse.UnknownError(RuntimeException("unknown error")) as DealsResponse)))

    // WHEN
    val stateObs = dealsListViewModel.stateObs().test()
    val eventObs = dealsListViewModel.eventObs().test()
    actionSignal.onNext(DealsListAction.Load)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListEvent.Snackbar(
        vm = SnackbarViewModel(
          messageResId = R.string.deals_get_unknown_error,
          duration = Snackbar.LENGTH_LONG
        )
      ))
  }

  @Test
  fun on_deal_click_action() {
    // GIVEN
    val dealsListViewModel = dealsListViewModel()
    val actionSignal = PublishSubject.create<DealsListAction>()
    dealsListViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = dealsListViewModel.stateObs().test()
    val eventObs = dealsListViewModel.eventObs().test()
    actionSignal.onNext(DealsListAction.DealClick(deal = deal))

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsListEvent.NavigateToDealDetails(id = deal.id))
  }
}