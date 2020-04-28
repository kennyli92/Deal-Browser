package com.target.dealbrowserpoc.deals.details

import com.target.dealbrowserpoc.dialog.SnackbarViewModel

sealed class DealDetailsEvent {
  object Noop : DealDetailsEvent()
  object Back : DealDetailsEvent()
  data class Snackbar(
    val vm: SnackbarViewModel
  ) : DealDetailsEvent()
}