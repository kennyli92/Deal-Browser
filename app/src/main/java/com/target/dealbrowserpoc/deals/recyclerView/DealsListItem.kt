package com.target.dealbrowserpoc.deals.recyclerView

import android.os.Parcelable
import com.target.dealbrowserpoc.deals.data.Deal
import kotlinx.android.parcel.Parcelize

sealed class DealsListItem : Parcelable {
  @Parcelize
  data class DealView(
    val deal: Deal
  ) : DealsListItem()
}