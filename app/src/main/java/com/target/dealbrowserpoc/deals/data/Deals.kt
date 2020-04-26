package com.target.dealbrowserpoc.deals.data

import com.squareup.moshi.Json

data class Deals(
  @Json(name = "_id") val id: String,
  val type: String,
  val data: List<Deal>
)