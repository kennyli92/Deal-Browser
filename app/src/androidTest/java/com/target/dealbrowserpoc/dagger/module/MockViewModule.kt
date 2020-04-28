package com.target.dealbrowserpoc.dagger.module

import com.nhaarman.mockitokotlin2.mock
import com.target.dealbrowserpoc.dagger.scope.ViewScope
import com.target.dealbrowserpoc.deals.data.DealsApi
import com.target.dealbrowserpoc.deals.data.DealsRepository
import dagger.Module
import dagger.Provides

@Module
class MockViewModule {

  private var dealsApi: DealsApi? = null
  @ViewScope
  @Provides
  fun providesDealsApi(): DealsApi {
    if (dealsApi == null) {
      dealsApi = mock()
    }

    return dealsApi!!
  }

  @ViewScope
  @Provides
  fun providesDealsRepository(
    dealsApi: DealsApi
  ): DealsRepository {
    return DealsRepository(dealsApi = dealsApi)
  }
}