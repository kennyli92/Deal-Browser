package com.target.dealbrowserpoc.deals.details

import androidx.lifecycle.SavedStateHandle
import com.google.android.material.snackbar.Snackbar
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.deals.data.DealsRepository
import com.target.dealbrowserpoc.deals.data.DealsResponse
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

class DealDetailsViewModelTest {
  @Before
  fun before() {
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
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

  private fun dealDetailsViewModel(): DealDetailsViewModel {
    return DealDetailsViewModel(
      handle = handle,
      dealsRepository = dealsRepository
    )
  }

  @Test
  fun on_load_action_when_get_deals_returns_data() {
    // GIVEN
    val dealDetailsViewModel = dealDetailsViewModel()
    val actionSignal = PublishSubject.create<DealDetailsAction>()
    dealDetailsViewModel.actionHandler(actionSignal = actionSignal)

    val dealList = listOf(deal, deal2)
    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(DealsResponse.Deals(deals = dealList) as DealsResponse)))

    // WHEN
    val stateObs = dealDetailsViewModel.stateObs().test()
    val eventObs = dealDetailsViewModel.eventObs().test()
    actionSignal.onNext(DealDetailsAction.Load(dealId = deal2.id))

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsState.Detail(
        imageUrl = deal2.image,
        salePrice = deal2.salePrice ?: deal2.price,
        regularPrice = deal2.price,
        title = deal2.title,
        description = deal2.description
      ))

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsEvent.Noop)
  }

  @Test
  fun on_load_action_when_get_deals_returns_not_found() {
    // GIVEN
    val dealDetailsViewModel = dealDetailsViewModel()
    val actionSignal = PublishSubject.create<DealDetailsAction>()
    dealDetailsViewModel.actionHandler(actionSignal = actionSignal)

    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(DealsResponse.NotFound as DealsResponse)))

    // WHEN
    val stateObs = dealDetailsViewModel.stateObs().test()
    val eventObs = dealDetailsViewModel.eventObs().test()
    actionSignal.onNext(DealDetailsAction.Load(dealId = deal2.id))

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsEvent.Snackbar(
        vm = SnackbarViewModel(
          messageResId = R.string.deal_details_get_not_found,
          duration = Snackbar.LENGTH_LONG
        )
      ))
  }

  @Test
  fun on_load_action_when_get_deals_returns_unknown_error() {
    // GIVEN
    val dealDetailsViewModel = dealDetailsViewModel()
    val actionSignal = PublishSubject.create<DealDetailsAction>()
    dealDetailsViewModel.actionHandler(actionSignal = actionSignal)

    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(
        DealsResponse.UnknownError(RuntimeException("unknown error")) as DealsResponse)))

    // WHEN
    val stateObs = dealDetailsViewModel.stateObs().test()
    val eventObs = dealDetailsViewModel.eventObs().test()
    actionSignal.onNext(DealDetailsAction.Load(dealId = deal2.id))

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsEvent.Snackbar(
        vm = SnackbarViewModel(
          messageResId = R.string.deal_details_get_unknown_error,
          duration = Snackbar.LENGTH_LONG
        )
      ))
  }

  @Test
  fun on_load_action_when_get_deals_returns_data_with_no_matching_deal() {
    // GIVEN
    val dealDetailsViewModel = dealDetailsViewModel()
    val actionSignal = PublishSubject.create<DealDetailsAction>()
    dealDetailsViewModel.actionHandler(actionSignal = actionSignal)

    val dealList = listOf(deal)
    whenever(dealsRepository.getDeals())
      .doReturn((Single.just(DealsResponse.Deals(deals = dealList) as DealsResponse)))

    // WHEN
    val stateObs = dealDetailsViewModel.stateObs().test()
    val eventObs = dealDetailsViewModel.eventObs().test()
    actionSignal.onNext(DealDetailsAction.Load(dealId = deal2.id))

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsEvent.Snackbar(
        vm = SnackbarViewModel(
          messageResId = R.string.deal_details_get_unknown_error,
          duration = Snackbar.LENGTH_LONG
        )
      ))
  }

  @Test
  fun on_back_click() {
    // GIVEN
    val dealDetailsViewModel = dealDetailsViewModel()
    val actionSignal = PublishSubject.create<DealDetailsAction>()
    dealDetailsViewModel.actionHandler(actionSignal = actionSignal)

    // WHEN
    val stateObs = dealDetailsViewModel.stateObs().test()
    val eventObs = dealDetailsViewModel.eventObs().test()
    actionSignal.onNext(DealDetailsAction.Back)

    // THEN
    stateObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsState.Noop)

    eventObs.assertNotComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealDetailsEvent.Back)
  }
}