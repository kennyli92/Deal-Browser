package com.target.dealbrowserpoc.deals.data

import io.reactivex.Single
import java.io.IOException

class DealsRepository(private val dealsApi: DealsApi) {
  fun getDeals(): Single<DealsResponse> {
    return dealsApi.getDeals()
      .map {
        val responseCode = it.response()?.code()
        when {
          responseCode == 200 && it.response()?.body() != null ->
            DealsResponse.Deals(deals = it.response()!!.body()!!.data)
          responseCode == 404 -> DealsResponse.NotFound
          !it.isError && responseCode != null ->
            DealsResponse.UnknownError(throwable = IOException("Unhandled code: $responseCode"))
          else -> DealsResponse.UnknownError(throwable = it.error()!!)
        }
      }.onErrorReturn { throwable ->
        DealsResponse.UnknownError(throwable = throwable)
      }
  }
}