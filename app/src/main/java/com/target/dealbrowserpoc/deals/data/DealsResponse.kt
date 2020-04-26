package com.target.dealbrowserpoc.deals.data

sealed class DealsResponse {
  data class Deals(
    val deals: List<Deal>
  ) : DealsResponse()

  object NotFound : DealsResponse()
  data class UnknownError(
    val throwable: Throwable
  ) : DealsResponse()
}