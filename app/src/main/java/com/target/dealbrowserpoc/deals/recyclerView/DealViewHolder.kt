package com.target.dealbrowserpoc.deals.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.Deal
import com.target.dealbrowserpoc.extensions.plusAssign
import com.target.dealbrowserpoc.log.Logging
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.item_deal.view.deal_container
import kotlinx.android.synthetic.main.item_deal.view.deal_image_view
import kotlinx.android.synthetic.main.item_deal.view.deal_price
import kotlinx.android.synthetic.main.item_deal.view.deal_store_aisle
import kotlinx.android.synthetic.main.item_deal.view.deal_title

class DealViewHolder(
  view: View,
  private val disposables: CompositeDisposable,
  private val dealClickObserver: Observer<Deal>
) : RecyclerView.ViewHolder(view) {
  fun bind(item: Deal) {
    Glide.with(itemView)
      .load(item.image)
      .placeholder(R.drawable.ic_error_outline_24dp)
      .error(R.drawable.ic_error_outline_24dp)
      .into(itemView.deal_image_view)

    itemView.deal_title.text = item.title
    itemView.deal_price.text = item.salePrice ?: item.price
    itemView.deal_store_aisle.text = item.aisle.capitalize()

    disposables += itemView.deal_container.clicks()
      .throttleLast(250, TimeUnit.MILLISECONDS)
      .subscribe({
        dealClickObserver.onNext(item)
      }, Logging.logErrorAndThrow())
  }
}