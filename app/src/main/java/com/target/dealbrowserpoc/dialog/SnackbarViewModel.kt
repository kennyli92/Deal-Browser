package com.target.dealbrowserpoc.dialog

import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT

data class SnackbarViewModel(
  val messageResId: Int = 0,
  val message: String = "",
  val duration: Int = LENGTH_SHORT,
  val actionTextResId: Int = 0,
  val actionText: String = "",
  val action: () -> Unit = {}
)