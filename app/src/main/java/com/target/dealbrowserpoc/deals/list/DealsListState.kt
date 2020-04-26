package com.target.dealbrowserpoc.deals.list

import com.target.dealbrowserpoc.deals.data.Deal

sealed class DealsListState {
  object Noop : DealsListState()
  data class Deals(
    val deals: List<Deal> = emptyList()
  ) : DealsListState()
}