package com.target.dealbrowserpoc.deals.details

sealed class DealDetailAction {
  data class Load(
    val dealId: String
  ) : DealDetailAction()
  object Back : DealDetailAction()
}