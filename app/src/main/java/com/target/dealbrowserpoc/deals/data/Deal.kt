package com.target.dealbrowserpoc.deals.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Deal(
  @Json(name = "_id") val id: String,
  val aisle: String,
  val description: String,
  val guid: String,
  val image: String,
  val index: Int,
  val price: String,
  val salePrice: String?,
  val title: String
) : Parcelable