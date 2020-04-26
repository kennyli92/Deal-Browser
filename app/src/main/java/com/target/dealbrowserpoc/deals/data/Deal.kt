package com.target.dealbrowserpoc.deals.data

import com.squareup.moshi.Json

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
)