package com.target.dealbrowserpoc.deals.recyclerView

import com.target.dealbrowserpoc.deals.data.Deal

sealed class DealsListItem {
  data class DealView(
    val deal: Deal
  ) : DealsListItem()
}