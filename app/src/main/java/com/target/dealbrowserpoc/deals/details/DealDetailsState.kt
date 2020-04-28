package com.target.dealbrowserpoc.deals.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class DealDetailsState : Parcelable {
  @Parcelize
  object Noop : DealDetailsState()
  @Parcelize
  data class Detail(
    val imageUrl: String,
    val salePrice: String,
    val regularPrice: String = "",
    val title: String,
    val description: String
  ) : DealDetailsState()
}