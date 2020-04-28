package com.target.dealbrowserpoc.stub

import com.target.dealbrowserpoc.deals.data.Deals

sealed class DealsState {
  data class GetDeals(
    val response: GetDealsResponse
  ) : DealsState()

  sealed class GetDealsResponse {
    data class Success(
      val deals: Deals
    ) : GetDealsResponse()
    object NotFound : GetDealsResponse()
    object UnknownError : GetDealsResponse()
  }
}