package com.target.dealbrowserpoc.deals.list

import com.target.dealbrowserpoc.deals.data.Deal

sealed class DealsListAction {
  object Load : DealsListAction()
  data class DealClick(
    val deal: Deal
  ) : DealsListAction()
}