package com.target.dealbrowserpoc.deals.list

import com.target.dealbrowserpoc.deals.recyclerView.DealsListItem

sealed class DealsListState {
  object Noop : DealsListState()
  data class ListItem(
    val dealsListItem: List<DealsListItem> = emptyList()
  ) : DealsListState()
}