package com.target.dealbrowserpoc.deals.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.target.dealbrowserpoc.dealbrowser.R
import com.target.dealbrowserpoc.deals.data.Deal
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable

class DealsListAdapter(
  private val disposables: CompositeDisposable,
  private val dealClickObserver: Observer<Deal>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var items: List<DealsListItem> = emptyList()

  override fun getItemCount(): Int {
    return items.size
  }

  override fun getItemViewType(position: Int): Int {
    return when (items[position]) {
      is DealsListItem.DealView -> R.layout.item_deal
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]

    when (holder) {
      is DealViewHolder -> holder.bind(item = (item as DealsListItem.DealView).deal)
      else -> throw IllegalArgumentException(
        "${this.javaClass.simpleName}: Unsupported Deals List View Holder!"
      )
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(viewType, parent, false)

    return when (viewType) {
      R.layout.item_deal -> DealViewHolder(
        view = view,
        disposables = disposables,
        dealClickObserver = dealClickObserver
      )
      else -> throw IllegalArgumentException(
        "${this.javaClass.simpleName}: Unsupported Deals List View Type!"
      )
    }
  }
}