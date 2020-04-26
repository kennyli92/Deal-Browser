package com.target.dealbrowserpoc.deals.data

import io.reactivex.Single
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.GET

interface DealsApi {
  @GET("api/deals")
  fun getDeals(): Single<Result<Deals>>
}