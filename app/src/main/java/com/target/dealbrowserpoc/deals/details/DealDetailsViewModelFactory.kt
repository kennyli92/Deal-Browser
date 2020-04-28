package com.target.dealbrowserpoc.deals.details

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.target.dealbrowserpoc.deals.data.DealsRepository

class DealDetailsViewModelFactory(
  private val dealsRepository: DealsRepository,
  owner: SavedStateRegistryOwner,
  defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
      key: String,
      modelClass: Class<T>,
      handle: SavedStateHandle
    ): T {
        return DealDetailsViewModel(handle = handle, dealsRepository = dealsRepository) as T
    }
}