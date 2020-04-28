package com.target.dealbrowserpoc.stub

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

class DealsStateStubber(
  private val mockViewModuleStubber: MockViewModuleStubber
) {
  fun onGetDeals(state: DealsState.GetDeals) {
    when (state.response) {
      is DealsState.GetDealsResponse.Success -> {
        whenever(mockViewModuleStubber.dealsApi.getDeals())
          .doReturn(Single.just(Result.response(Response.success(state.response.deals))))
      }
      is DealsState.GetDealsResponse.NotFound -> {
      }
      is DealsState.GetDealsResponse.UnknownError -> {
      }
    }
  }
}