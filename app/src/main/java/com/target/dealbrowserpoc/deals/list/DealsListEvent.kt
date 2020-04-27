package com.target.dealbrowserpoc.deals.list

import com.target.dealbrowserpoc.dialog.SnackbarViewModel

sealed class DealsListEvent {
  object Noop : DealsListEvent()
  data class NavigateToDealDetails(
    val id: String
  ) : DealsListEvent()
  data class Snackbar(
    val vm: SnackbarViewModel
  ) : DealsListEvent()
}