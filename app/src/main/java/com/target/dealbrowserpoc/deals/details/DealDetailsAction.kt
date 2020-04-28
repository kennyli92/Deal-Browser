package com.target.dealbrowserpoc.deals.details

sealed class DealDetailsAction {
  data class Load(
    val dealId: String
  ) : DealDetailsAction()
  object Back : DealDetailsAction()
}