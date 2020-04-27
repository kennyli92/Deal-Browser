package com.target.dealbrowserpoc.deals.list

sealed class DealsListEvent {
  object Noop : DealsListEvent()
  data class NavigateToDealDetails(
    val id: String
  ) : DealsListEvent()
}