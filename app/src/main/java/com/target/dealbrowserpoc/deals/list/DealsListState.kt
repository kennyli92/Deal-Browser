package com.target.dealbrowserpoc.deals.list

import android.os.Parcelable
import com.target.dealbrowserpoc.deals.recyclerView.DealsListItem
import kotlinx.android.parcel.Parcelize

sealed class DealsListState : Parcelable {
  @Parcelize
  object Noop : DealsListState()
  @Parcelize
  data class ListItem(
    val dealsListItem: List<DealsListItem> = emptyList()
  ) : DealsListState()
}