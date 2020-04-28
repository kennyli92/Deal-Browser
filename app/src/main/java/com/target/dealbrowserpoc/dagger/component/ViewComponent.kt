package com.target.dealbrowserpoc.dagger.component

import com.target.dealbrowserpoc.deals.details.DealDetailsFragment
import com.target.dealbrowserpoc.deals.list.DealsListFragment

/**
 * Only put inject methods here
 */
interface ViewComponent {
  fun inject(dealsListFragment: DealsListFragment)
  fun inject(dealDetailsFragment: DealDetailsFragment)
}