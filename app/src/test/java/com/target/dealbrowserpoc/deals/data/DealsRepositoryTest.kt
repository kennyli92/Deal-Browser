package com.target.dealbrowserpoc.deals.data

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class DealsRepositoryTest {
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

  private val dealsApi: DealsApi = mock()

  private fun dealsRepository(): DealsRepository {
    return DealsRepository(
      dealsApi = dealsApi
    )
  }

  @Test
  fun get_deals_on_200() {
    // GIVEN
    val dealsRepository = dealsRepository()
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
    val deals = Deals(
      id = "id",
      type = "list",
      data = dealList
    )
    val successResponse = Response.success(deals)
    whenever(dealsApi.getDeals())
      .doReturn(Single.just(Result.response(successResponse)))

    // WHEN
    dealsRepository.getDeals().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsResponse.Deals(deals = dealList))
  }

  @Test
  fun get_deals_on_404() {
    // GIVEN
    val dealsRepository = dealsRepository()
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"), "content")
    val notFoundResponse = Response.error<Deals>(404, responseBody)
    whenever(dealsApi.getDeals())
      .doReturn(Single.just(Result.response(notFoundResponse)))

    // WHEN
    dealsRepository.getDeals().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsResponse.NotFound)
  }

  @Test
  fun get_deals_on_unhandled_error_code() {
    // GIVEN
    val dealsRepository = dealsRepository()
    val responseBody = ResponseBody.create(
      MediaType.parse("application/json"), "content")
    val unhandledResponse = Response.error<Deals>(500, responseBody)
    whenever(dealsApi.getDeals())
      .doReturn(Single.just(Result.response(unhandledResponse)))

    // WHEN
    val values = dealsRepository.getDeals().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .values()

    assertThat(values[0]).isInstanceOf(DealsResponse.UnknownError::class.java)
    assertThat((values[0] as DealsResponse.UnknownError).throwable.message)
      .isEqualTo("Unhandled code: 500")
  }

  @Test
  fun get_deals_on_unhandled_error() {
    // GIVEN
    val dealsRepository = dealsRepository()
    val unhandledException = RuntimeException("idk")
    whenever(dealsApi.getDeals())
      .doReturn(Single.just(Result.error(unhandledException)))

    // WHEN
    dealsRepository.getDeals().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsResponse.UnknownError(throwable = unhandledException))
  }

  @Test
  fun get_deals_on_network_call_error() {
    // GIVEN
    val dealsRepository = dealsRepository()
    val unhandledIOException = IOException("idk")
    whenever(dealsApi.getDeals())
      .doReturn(Single.error(unhandledIOException))

    // WHEN
    dealsRepository.getDeals().test()
      .assertComplete()
      .assertNoErrors()
      .assertValueCount(1)
      .assertValueAt(0, DealsResponse.UnknownError(throwable = unhandledIOException))
  }
}